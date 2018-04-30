package com.example.santiago.cuantassabes.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.dpro.widgets.WeekdaysPicker;
import com.example.santiago.cuantassabes.LoginActivity;
import com.example.santiago.cuantassabes.R;
import com.example.santiago.cuantassabes.model.Doctor;
import com.example.santiago.cuantassabes.model.Pacient;
import com.example.santiago.cuantassabes.ui.CircleTransform;
import com.franmontiel.fullscreendialog.FullScreenDialogContent;
import com.franmontiel.fullscreendialog.FullScreenDialogController;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rilixtech.CountryCodePicker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adil.dev.lib.materialnumberpicker.dialog.GenderPickerDialog;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;


public class FragmentFormDoctor extends Fragment implements FullScreenDialogContent {
    private static final String TAG = FragmentFormDoctor.class.getSimpleName();
    private static final int RC_PICK_IMAGE = 1;

    @BindView(R.id.scroll_container)
    ScrollView scrollViewContainer;
    @BindView(R.id.user_doctor_image)
    ImageView doctorUserImage;
    @BindView(R.id.edit_text_name)
    EditText editTextName;
    @BindView(R.id.edit_text_email)
    EditText editTextEmail;
    @BindView(R.id.ccp_doctor)
    CountryCodePicker ccp;
    @BindView(R.id.edit_text_phone_number)
    EditText editTextPhoneNumber;
    @BindView(R.id.text_input_layout_gender)
    TextInputLayout textInputLayoutGender;
    @BindView(R.id.edit_text_gender)
    EditText editTextGender;
    @BindView(R.id.edit_text_university)
    EditText editTextUniversity;
    @BindView(R.id.edit_text_office_address)
    EditText editTextOfficeAddress;
    @BindView(R.id.days_attention)
    WeekdaysPicker daysAttention;
    @BindView(R.id.edit_text_attention_hours)
    EditText editTextAttentionHours;
    @BindView(R.id.edit_text_appointment_cost)
    EditText editTextAppointmentCost;
    @BindView(R.id.checkbox_term_conditions)
    CheckBox checkBoxTermsConditions;

    private String userId;
    private String userName;
    private String userEmail;
    private String doctorImageUrl;

    private Doctor doctorToInsert;

    OnFormDoctorCorrect onFormDoctorCorrect;

    //Firebase instances
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mPhotosStorageReference;

    public interface OnFormDoctorCorrect {
        void setDoctor(Doctor doctor);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onFormDoctorCorrect = (OnFormDoctorCorrect) context;
        } catch (Exception e) {
            Log.d(TAG, "onAttach: " + e.getMessage());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_form_doctor, container, false);
        ButterKnife.bind(this, rootView);
        mFirebaseStorage = FirebaseStorage.getInstance();
        ccp.registerPhoneNumberTextView(editTextPhoneNumber);
        Bundle bundle = getArguments();
        if (bundle != null) {
            userName = bundle.getString("name");
            userEmail = bundle.getString("email");
            editTextName.setText(userName);
            editTextEmail.setText(userEmail);
        }
        doctorUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RC_PICK_IMAGE);
            }
        });

        textInputLayoutGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderListener();
            }
        });
        editTextGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderListener();
            }
        });
        editTextAttentionHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {
                                String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
                                String minuteString = minute < 10 ? "0"+minute : ""+minute;
                                String hourStringEnd = hourOfDayEnd < 10 ? "0"+hourOfDayEnd : ""+hourOfDayEnd;
                                String minuteStringEnd = minuteEnd < 10 ? "0"+minuteEnd : ""+minuteEnd;
                                String time = "You picked the following time: From - "+hourString+"h"+minuteString+" To - "+hourStringEnd+"h"+minuteStringEnd;
                                editTextAttentionHours.setText(hourString+":"+minuteString+" - "+hourStringEnd+":"+minuteStringEnd);
                            }
                        },
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );
                tpd.show(getActivity().getFragmentManager(),"");
            }
        });
        editTextOfficeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return rootView;
    }

    private boolean addNewDoctor() {
        String imageUrl = "";
        if (doctorImageUrl!=null){
            if (!doctorImageUrl.isEmpty()){
                imageUrl = doctorImageUrl;
            }
        }
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String phoneNumber = ccp.getSelectedCountryCodeWithPlus()+editTextPhoneNumber.getText().toString();
        String gender = editTextGender.getText().toString();
        String university = editTextUniversity.getText().toString();
        String officeAddress = editTextOfficeAddress.getText().toString();
        List<String> daysAttentionList = daysAttention.getSelectedDaysText();
        String hoursAttention = editTextAttentionHours.getText().toString();
        String appointmentCost = editTextAppointmentCost.getText().toString();
        List<Pacient> pacients = new ArrayList<>();
        if (!name.isEmpty() && !email.isEmpty() && !phoneNumber.isEmpty() &&
                !gender.isEmpty() && !officeAddress.isEmpty() && !daysAttentionList.isEmpty() &&
                !hoursAttention.isEmpty()) {
            if (checkBoxTermsConditions.isChecked()) {
                doctorToInsert = new Doctor(imageUrl, name, email, phoneNumber, gender, university, officeAddress, daysAttentionList, hoursAttention, appointmentCost, pacients);
                onFormDoctorCorrect.setDoctor(doctorToInsert);
                Snackbar.make(scrollViewContainer, "Registro Correcto", Snackbar.LENGTH_SHORT).show();
                return true;
            } else {
                Snackbar.make(scrollViewContainer, "Completa todos los datos *", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            Snackbar.make(scrollViewContainer, "Completa todos los datos *", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }

    private void genderListener() {
        GenderPickerDialog dialog = new GenderPickerDialog(getContext());
        dialog.setOnSelectingGender(new GenderPickerDialog.OnGenderSelectListener() {
            @Override
            public void onSelectingGender(String value) {
                if (value.equals("M")) {
                    editTextGender.setText("Masculino");
                } else if (value.equals("F")) {
                    editTextGender.setText("Femenino");
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onDialogCreated(FullScreenDialogController dialogController) {

    }

    @Override
    public boolean onConfirmClick(FullScreenDialogController dialogController) {
        addNewDoctor();
        dialogController.confirm(null);
        return true;
    }

    @Override
    public boolean onDiscardClick(FullScreenDialogController dialogController) {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                mPhotosStorageReference = mFirebaseStorage.getReference().child("doctor_images").child(userName);
                mPhotosStorageReference.putFile(selectedImage)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                doctorImageUrl = taskSnapshot.getDownloadUrl().toString();
                                Picasso.get().load(Uri.parse(doctorImageUrl)).transform(new CircleTransform()).into(doctorUserImage);
                                Snackbar.make(scrollViewContainer,"insertada ="+doctorImageUrl,Snackbar.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

}
