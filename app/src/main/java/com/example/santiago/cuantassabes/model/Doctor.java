package com.example.santiago.cuantassabes.model;

import java.util.List;

public class Doctor {
    private String photoUrl;
    private String name;
    private String email;
    private String phoneNumber;
    private String gender;
    private String university;
    private String officeAddress;
    private List<String> daysAtenttion;
    private String hoursAtenttion;
    private String appointmentCost;


    private List<Pacient> pacients;
    public Doctor(){}

    public Doctor(String photoUrl, String name, String email, String phoneNumber,
                  String gender, String university, String officeAddress,
                  List<String> daysAtenttion, String hoursAtenttion, String appointmentCost, List<Pacient> pacients){
        this.photoUrl = photoUrl;

        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.university = university;
        this.officeAddress = officeAddress;
        this.daysAtenttion =daysAtenttion;
        this.hoursAtenttion = hoursAtenttion;
        this.appointmentCost = appointmentCost;
        this.pacients = pacients;


    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public String getUniversity() {
        return university;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public List<String> getDaysAtenttion() {
        return daysAtenttion;
    }

    public String getHoursAtenttion() {
        return hoursAtenttion;
    }

    public String getAppointmentCost() {
        return appointmentCost;
    }

    public List<Pacient> getPacients() {
        return pacients;
    }


    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

}
