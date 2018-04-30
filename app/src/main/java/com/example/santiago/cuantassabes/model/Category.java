package com.example.santiago.cuantassabes.model;

public class Category {
    private String ageParent;
    private String categoryName;
    private String photoUrl;


    public Category(String ageParent,String categoryName, String photoUrl){
        this.photoUrl = photoUrl;
        this.ageParent = ageParent;
        this.categoryName = categoryName;

    }
    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getAgeParent() {
        return ageParent;
    }
}
