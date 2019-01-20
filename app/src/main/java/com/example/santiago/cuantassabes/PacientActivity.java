package com.example.santiago.cuantassabes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.santiago.cuantassabes.fragments.pacient.PacientNavigation;
import com.firebase.ui.auth.AuthUI;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PacientActivity extends AppCompatActivity {
    @BindView(R.id.close_button)
    ImageView closeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacient);
        ButterKnife.bind(this);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(PacientActivity.this);
            }
        });
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
