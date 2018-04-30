package com.example.santiago.cuantassabes.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.example.santiago.cuantassabes.R;
import com.example.santiago.cuantassabes.model.Pacient;
import com.example.santiago.cuantassabes.ui.CircleTransform;
import com.franmontiel.fullscreendialog.FullScreenDialogContent;
import com.franmontiel.fullscreendialog.FullScreenDialogController;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rilixtech.CountryCodePicker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import adil.dev.lib.materialnumberpicker.dialog.GenderPickerDialog;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class FragmentFormPacient extends Fragment implements FullScreenDialogContent {
    private static final String TAG = FragmentFormPacient.class.getSimpleName();
    private static final int RC_PICK_IMAGE = 1;

    @BindView(R.id.pacient_user_image)
    ImageView pacientUserImage;
    @BindView(R.id.container_pacient_form)
    ScrollView containerPacientForm;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.checkBoxHasDoctor)
    CheckBox checkBoxHasDoctor;
    @BindView(R.id.editTextPacientName)
    EditText editTextPacientName;
    @BindView(R.id.editTextPacientGender)
    EditText editTextPacientGender;
    @BindView(R.id.editTextPacientAge)
    EditText editTextPacientAge;
    @BindView(R.id.editTextPacientHeight)
    EditText editTextPacientHeight;
    @BindView(R.id.editTextPacientWeight)
    EditText editTextPacientWeight;
    @BindView(R.id.editTextGuardianName)
    EditText editTextGuardianName;
    @BindView(R.id.editTextGuardianEmail)
    EditText editTextGuardianEmail;
    @BindView(R.id.editTextRelationship)
    EditText editTextRelationShip;
    @BindView(R.id.editTextGuardianPhone)
    EditText editTextGuardianPhone;
    @BindView(R.id.editTextGuardianAddress)
    EditText editTextGuardianAddress;
    @BindView(R.id.ccp)
    CountryCodePicker countryCodePicker;
    @BindView(R.id.checkBoxTermsConditionsPacient)
    CheckBox checkBoxTermsConditions;


    private List<String> doctorsAvailable;
    private Pacient pacientToInsert;
    private String username;
    private String email;
    OnFormCorrect onFormCorrect;
    private String pacientImageUrl = "";
    //Firebase Instances
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mPhotosStorageReference;

    public interface OnFormCorrect {
        void setData(Pacient pacient);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onFormCorrect = (OnFormCorrect) context;
        } catch (Exception e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_form_pacient, container, false);
        ButterKnife.bind(this, rootView);
        countryCodePicker.registerPhoneNumberTextView(editTextGuardianPhone);
        doctorsAvailable = new ArrayList<>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("doctors");
        Bundle bundle = getArguments();
        if (bundle!=null){
            username = bundle.getString("name");
            email = bundle.getString("email");
            editTextGuardianName.setText(username);
            editTextGuardianEmail.setText(email);
        }

        pacientUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RC_PICK_IMAGE);
            }
        });
        checkBoxHasDoctor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spinner.setVisibility(View.GONE);
                } else {
                    spinner.setVisibility(View.VISIBLE);
                }
            }
        });
        editTextPacientGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderListener();
            }
        });
        editTextGuardianAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        List<String> noDoctorFound = new ArrayList<>();
        noDoctorFound.add("No hay doctores disponibles");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, noDoctorFound);
        spinner.setAdapter(arrayAdapter);
        attachDatabaseListener();
        return rootView;
    }

    private void attachDatabaseListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    doctorsAvailable.add(dataSnapshot.getKey());
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, doctorsAvailable);
                    spinner.setAdapter(arrayAdapter);
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

    private boolean insertPacient() {
        String imageUrl = "" ;
        if (!pacientImageUrl.isEmpty()){
            imageUrl = pacientImageUrl;
        }
        String doctorSelected = "";
        if (!checkBoxHasDoctor.isChecked()){
            doctorSelected = spinner.getSelectedItem().toString();
        }
        String name = editTextPacientName.getText().toString();
        String gender = editTextPacientGender.getText().toString();
        String age = editTextPacientAge.getText().toString();
        String height = editTextPacientHeight.getText().toString();
        String weight = editTextPacientWeight.getText().toString();
        String guardianName = editTextGuardianName.getText().toString();
        String guardianEmail = editTextGuardianEmail.getText().toString();
        String guardianRelationship = editTextRelationShip.getText().toString();
        String guardianPhone = countryCodePicker.getSelectedCountryCodeWithPlus() + editTextGuardianPhone.getText().toString();
        String guardianAddress = editTextGuardianAddress.getText().toString();
        if (checkBoxTermsConditions.isChecked()) {
            if (!name.isEmpty() && !gender.isEmpty() && !age.isEmpty() && !guardianName.isEmpty() && !guardianEmail.isEmpty() && !guardianRelationship.isEmpty() && !guardianPhone.isEmpty() && !guardianAddress.isEmpty()) {
                pacientToInsert = new Pacient(imageUrl,doctorSelected, name, gender, age, height, weight, guardianName, guardianEmail, guardianRelationship, guardianPhone, guardianAddress);
                onFormCorrect.setData(pacientToInsert);
                return true;
            } else {
                Snackbar.make(containerPacientForm, "Completa todos los datos *", Snackbar.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Snackbar.make(containerPacientForm, "Debes aceptar terminos y condiciones", Snackbar.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onDialogCreated(FullScreenDialogController dialogController) {

    }

    @Override
    public boolean onConfirmClick(FullScreenDialogController dialogController) {
        boolean pacientInserted = insertPacient();
        if (pacientInserted) {
            dialogController.confirm(null);
        }
        return true;
    }

    @Override
    public boolean onDiscardClick(final FullScreenDialogController dialogController) {
        new AlertDialog.Builder(getContext())
                .setTitle("")
                .setMessage("Hola, podras salir hasta completar el formulario")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogController.confirm(null);
                        //quitar esta linea
                    }
                }).show();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                mPhotosStorageReference = mFirebaseStorage.getReference().child("pacient_images").child(username);
                mPhotosStorageReference.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                pacientImageUrl = taskSnapshot.getDownloadUrl().toString();
                                Picasso.get().load(Uri.parse(pacientImageUrl)).transform(new CircleTransform()).into(pacientUserImage);
                                Snackbar.make(containerPacientForm,"insertada ="+pacientImageUrl,Snackbar.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    private void genderListener() {
        GenderPickerDialog dialog = new GenderPickerDialog(getContext());
        dialog.setOnSelectingGender(new GenderPickerDialog.OnGenderSelectListener() {
            @Override
            public void onSelectingGender(String value) {
                if (value.equals("M")) {
                    editTextPacientGender.setText("Masculino");
                } else if (value.equals("F")) {
                    editTextPacientGender.setText("Femenino");
                }
            }
        });
        dialog.show();
    }

}
