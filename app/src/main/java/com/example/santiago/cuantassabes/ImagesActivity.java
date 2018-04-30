package com.example.santiago.cuantassabes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.santiago.cuantassabes.fragments.pacient.ImagesList;

public class ImagesActivity extends AppCompatActivity {
    private static final String TAG = ImagesActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        Intent intent = getIntent();
        String age = intent.getStringExtra("age");
        String category = intent.getStringExtra("name");

        ImagesList imagesList = new ImagesList();
        imagesList.setData(age,category);
        getSupportFragmentManager().beginTransaction().add(R.id.images_container, imagesList).commit();
    }
}