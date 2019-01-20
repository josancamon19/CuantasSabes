package com.example.santiago.cuantassabes;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.santiago.cuantassabes.fragments.FragmentFormPacient;
import com.example.santiago.cuantassabes.fragments.FragmentFormDoctor;
import com.example.santiago.cuantassabes.model.Doctor;
import com.example.santiago.cuantassabes.model.Pacient;
import com.firebase.ui.auth.AuthUI;
import com.franmontiel.fullscreendialog.FullScreenDialogFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;

public class LoginActivity extends AppCompatActivity implements VerticalStepperForm, FragmentFormPacient.OnFormCorrect, FragmentFormDoctor.OnFormDoctorCorrect {
    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.stepper_container)
    RelativeLayout stepperContainer;
    @BindView(R.id.vertical_stepper_form)
    VerticalStepperFormLayout stepperLayout;

    private String mUsername;
    private String mUserEmail;
    private String mUserId;
    private String userAge;
    private int doctorOrPacient = 1;
    private Pacient pacientToInsert;
    private Doctor doctorToInsert;

    private static final int RC_SIGN_IN = 123;
    //Firebase Instances
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        String[] mySteps = {"Registro exitoso", "Doctor o Paciente", "Datos Adicionales"};
        String[] subtitles = {"Ya eres parte de nuestra comunidad!", "Selecciona una de las opciones", "Completa la siguiente informacion"};
        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.primary);
        int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.primary_dark);
        VerticalStepperFormLayout.Builder.newInstance(stepperLayout, mySteps, this, this)
                .primaryColor(colorPrimary)
                .stepsSubtitles(subtitles)
                .primaryDarkColor(colorPrimaryDark)
                .displayBottomNavigation(true).init(); // It is true by default, so in this case this line is not necessary

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    final String userEmail = user.getEmail();
                    final String id = user.getProviderId();
                    final String username = user.getDisplayName();
                    onSignedInInitialize(id, username, userEmail);
                    boolean exist =checkUserLogin(username);
                    /*if (exist){
                        Intent intent = new Intent(LoginActivity.this,PacientActivity.class);
                        intent.putExtra("age","3");
                        intent.putExtra("username",username);
                        startActivity(intent);
                    }*/
                    // User is signed in
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false, true)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.FacebookBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .setLogo(R.drawable.log_216)
                                    .setTheme(R.style.Theme_AppCompat_Light_NoActionBar)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };


        OnCompleteListener<AuthResult> completeListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                    Log.d(TAG, "onComplete: " + isNew);
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private void onSignedInInitialize(String userId, String username, String userEmail) {
        mUsername = username;
        mUserEmail = userEmail;
        mUserId = userId;
    }

    private void onSignedOutCleanup() {
        mUsername = "";
    }

    private boolean checkUserLogin(String mUsername) {
        mDatabaseReference = mFirebaseDatabase.getReference().child("doctors").child(mUsername);
        if (mDatabaseReference!=null){
            return true;
        }else{
            //si no existe en doctor ahora si hay que recorrer todos los doctores y comprobar que el paciente exista o no
            return false;
        }
    }

    @Override
    public View createStepContentView(int stepNumber) {
        View view = null;
        switch (stepNumber) {
            case 0:
                view = LayoutInflater.from(getBaseContext()).inflate(R.layout.step_01, null, false);
                break;
            case 1:
                view = LayoutInflater.from(getBaseContext()).inflate(R.layout.step_02, null, false);
                RadioButton doctor = view.findViewById(R.id.radioButton);
                RadioButton pacient = view.findViewById(R.id.radioButton2);
                pacient.setChecked(true);
                doctor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            doctorOrPacient = 0;
                        } else {
                            doctorOrPacient = 1;
                        }
                    }
                });
                break;
            case 2:
                view = LayoutInflater.from(getBaseContext()).inflate(R.layout.step_03, null, false);
                break;
        }
        return view;
    }

    @Override
    public void onStepOpening(int stepNumber) {
        switch (stepNumber) {
            case 0:
                stepperLayout.setActiveStepAsCompleted();
                break;
            case 1:
                stepperLayout.setActiveStepAsCompleted();
                break;
            case 2:
                Bundle bundle = new Bundle();
                bundle.putString("name", mUsername);
                bundle.putString("email", mUserEmail);
                if (doctorOrPacient == 0) {
                    FullScreenDialogFragment du = new FullScreenDialogFragment.Builder(LoginActivity.this)
                            .setTitle("Datos Adicionales")
                            .setConfirmButton("Enviar")
                            .setOnConfirmListener(null)
                            .setOnDiscardListener(null)
                            .setContent(FragmentFormDoctor.class, bundle)
                            .build();
                    du.show(getSupportFragmentManager(), "fragment");
                } else {
                    FullScreenDialogFragment du = new FullScreenDialogFragment.Builder(LoginActivity.this)
                            .setTitle("Datos Adicionales")
                            .setConfirmButton("Enviar")
                            .setOnConfirmListener(null)
                            .setOnDiscardListener(null)
                            .setContent(FragmentFormPacient.class, bundle)
                            .build();
                    du.show(getSupportFragmentManager(), "fragment");
                }

                stepperLayout.setActiveStepAsCompleted();
                break;
            case 3:
                break;
        }
    }

    @Override
    public void sendData() {
        switch (doctorOrPacient) {
            case 0:
                insertDoctor(doctorToInsert);
                Intent intentDoctor = new Intent(this, DoctorActivity.class);
                startActivity(intentDoctor);
                break;
            case 1:
                insertPacient(pacientToInsert);
                Intent intentPacient = new Intent(this, PacientActivity.class);
                intentPacient.putExtra("age","3");
                startActivity(intentPacient);
                break;
        }
    }


    @Override
    public void setData(Pacient pacient) {
        pacientToInsert = pacient;
        Toast.makeText(getBaseContext(), pacient.getPacientName(), Toast.LENGTH_SHORT).show();
    }

    private void insertPacient(Pacient pacient) {
        if (pacient != null) {
            if (!pacient.getDoctorSelected().isEmpty()) {
                mDatabaseReference = mFirebaseDatabase.getReference().child("doctors").child(pacient.getDoctorSelected()).child("pacients").child(pacient.getPacientName());
            } else {
                mDatabaseReference = mFirebaseDatabase.getReference().child("doctors").child("other-pacients").child(pacient.getPacientName());
            }
            mDatabaseReference.setValue(pacient);
            Toast.makeText(getBaseContext(), "paciente insertado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setDoctor(Doctor doctor) {
        doctorToInsert = doctor;
        Toast.makeText(this, doctor.getEmail(), Toast.LENGTH_SHORT).show();
    }

    private void insertDoctor(Doctor doctor) {
        if (doctor != null) {
            mDatabaseReference = mFirebaseDatabase.getReference().child("doctors").child(doctor.getName());
            mDatabaseReference.setValue(doctor);
        }
    }

}
