package com.example.santiago.cuantassabes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.santiago.cuantassabes.fragments.pacient.Quiz;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Intent intent = getIntent();
        String age = intent.getStringExtra("age");
        Quiz fragmentQuiz = new Quiz();
        fragmentQuiz.setData(age);
        getSupportFragmentManager().beginTransaction().add(R.id.quiz_container,fragmentQuiz).commit();
    }
}
