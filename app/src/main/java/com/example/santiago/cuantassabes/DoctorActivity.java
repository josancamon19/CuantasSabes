package com.example.santiago.cuantassabes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.santiago.cuantassabes.fragments.doctor.FragmentPacientList;

public class DoctorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        getSupportFragmentManager().beginTransaction().add(R.id.pacients_container,new FragmentPacientList()).commit();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Toast.makeText(this,"homeclicked",Toast.LENGTH_SHORT).show();
                //implement close session
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }
}
