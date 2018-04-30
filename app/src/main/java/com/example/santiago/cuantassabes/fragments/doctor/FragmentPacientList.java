package com.example.santiago.cuantassabes.fragments.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.santiago.cuantassabes.PacientDetailsActivity;
import com.example.santiago.cuantassabes.R;
import com.example.santiago.cuantassabes.adapters.RecyclerPacientsAdapter;
import com.example.santiago.cuantassabes.adapters.RecyclerPacientsAdapter.OnPacientClick;
import com.example.santiago.cuantassabes.model.Pacient;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class FragmentPacientList extends Fragment implements OnPacientClick {
    private static final int RC_SIGN_IN = 123;
    @BindView(R.id.recycler_pacients)
    RecyclerView recyclerPacients;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    RecyclerPacientsAdapter recyclerPacientsAdapter;
    private List<Pacient> pacientList;
    private String doctorUsername;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ChildEventListener mChildEventListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pacient_list,container,false);
        ButterKnife.bind(this,rootView);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        recyclerPacientsAdapter = new RecyclerPacientsAdapter(getContext(),this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerPacients.setHasFixedSize(true);
        recyclerPacients.setLayoutManager(linearLayoutManager);
        recyclerPacients.setAdapter(recyclerPacientsAdapter);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    String userEmail = user.getEmail();
                    String id = user.getProviderId();
                    String username = user.getDisplayName();
                    mDatabaseReference = mFirebaseDatabase.getReference().child("doctors").child(username).child("pacients");
                    attachDatabasereadListener();

                    // User is signed in
                } else {
                    // User is signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false, true)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(getContext(), "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(getContext(), "Sign in canceled", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }
    private void attachDatabasereadListener(){
        if (mChildEventListener ==null){
            pacientList = new ArrayList<>();
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Pacient pacient = dataSnapshot.getValue(Pacient.class);
                    pacientList.add(pacient);
                    recyclerPacientsAdapter.setData(pacientList);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    @Override
    public void setOnPacientClick(Pacient pacient) {
        String photoUrl = pacient.getPhotoUrl();
        String doctorSelected = pacient.getDoctorSelected();
        String name = pacient.getPacientName();
        String gender = pacient.getPacientGender();
        String age = pacient.getPacientAge();
        String height = pacient.getPacientHeight();
        String weight = pacient.getPacientWeight();
        String guardianName = pacient.getGuardianName();
        String guardianEmail = pacient.getGuardianEmail();
        String guardianRelationship = pacient.getGuardianRelationship();
        String guardianPhone = pacient.getGuardianPhone();
        String guardianAddress = pacient.getGuardianAddress();

        Intent intent = new Intent(getActivity(), PacientDetailsActivity.class);
        intent.putExtra("photoUrl",photoUrl);
        intent.putExtra("doctor_selected",doctorSelected);
        intent.putExtra("name",name);
        intent.putExtra("gender",gender);
        intent.putExtra("age",age);
        intent.putExtra("height",height);
        intent.putExtra("weight",weight);
        intent.putExtra("guardian_name",guardianName);
        intent.putExtra("guardian_email",guardianEmail);
        intent.putExtra("guardian_relationship",guardianRelationship);
        intent.putExtra("guardian_phone",guardianPhone);
        intent.putExtra("guardian_address",guardianAddress);
        startActivity(intent);

    }
}
