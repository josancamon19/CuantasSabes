package com.example.santiago.cuantassabes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.santiago.cuantassabes.fragments.pacient.PacientNavigation;

public class PacientActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacient);
        Intent intent  = getIntent();
        PacientNavigation fragmentPacientNavigation = new PacientNavigation();
        fragmentPacientNavigation.setAge(intent.getStringExtra("age"));
        fragmentPacientNavigation.setUser(intent.getStringExtra("username"));
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragmentPacientNavigation).commit();
    }
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
