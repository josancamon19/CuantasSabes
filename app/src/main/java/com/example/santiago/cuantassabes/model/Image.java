package com.example.santiago.cuantassabes.model;

import java.util.List;

public class Image {
    private String name;
    private String photoUrl;
    private List<String> answers;
    //this other constructor is necessary by the cast in the line Image image = datasnapshot.getvalue(Image.class);
    public Image(){}
    public Image(String name, String photoUrl, List<String> answers){
        this.name = name;
        this.photoUrl = photoUrl;
        this.answers = answers;
    }
    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public List<String> getAnswers() {
        return answers;
    }

}
