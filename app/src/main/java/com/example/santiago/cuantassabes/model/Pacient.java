package com.example.santiago.cuantassabes.model;

public class Pacient {
    private String photoUrl;
    private String doctorSelected;
    private String pacientName;
    private String pacientGender;
    private String pacientAge;
    private String pacientHeight;
    private String pacientWeight;
    private String guardianName;
    private String guardianEmail;
    private String guardianRelationship;
    private String guardianPhone;
    private String guardianAddress;
    public Pacient(){

    }

    public Pacient(String photoUrl, String doctorSelected, String pacientName, String pacientgender, String pacientAge, String pacientHeight, String pacientWeight, String guardianName, String guardianEmail, String guardianRelationship, String guardianPhone, String guardianAddress) {
        this.photoUrl = photoUrl;
        this.doctorSelected = doctorSelected;
        this.pacientName = pacientName;
        this.pacientGender = pacientgender;
        this.pacientAge = pacientAge;
        this.pacientHeight = pacientHeight;
        this.pacientWeight = pacientWeight;
        this.guardianName = guardianName;
        this.guardianEmail = guardianEmail;
        this.guardianRelationship = guardianRelationship;

        this.guardianPhone = guardianPhone;
        this.guardianAddress = guardianAddress;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getPacientName() {
        return pacientName;
    }

    public String getPacientGender() {
        return pacientGender;
    }

    public String getPacientAge() {
        return pacientAge;
    }

    public String getPacientHeight() {
        return pacientHeight;
    }

    public String getPacientWeight() {
        return pacientWeight;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public String getGuardianEmail() {
        return guardianEmail;
    }

    public String getGuardianPhone() {
        return guardianPhone;
    }

    public String getGuardianAddress() {
        return guardianAddress;
    }

    public String getDoctorSelected() {
        return doctorSelected;
    }

    public String getGuardianRelationship() {
        return guardianRelationship;
    }

}
