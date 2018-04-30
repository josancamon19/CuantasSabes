package com.example.santiago.cuantassabes.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;

public class StepperAdapter extends AbstractFragmentStepAdapter {
    private String userId;
    private String userName;
    private String userEmail;
    public StepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        switch (position) {
            case 0:
                /*FragmentFormDoctor step = new FragmentFormDoctor();
                Bundle b = new Bundle();
                b.putInt("position", position);
                step.setArguments(b);
                return step;*/
            case 1:
                /*FragmentFormDoctor step1 = new FragmentFormDoctor();
                step1.setDataFromFirebase(userId, userName, userEmail);
                //FragmentFormPacient step1 = new FragmentFormPacient();
                Bundle b1= new Bundle();
                b1.putInt("position", position);
                step1.setArguments(b1);
                return step1;*/
            default:
                //return new FragmentFormDoctor();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
    public void setData(String userId,String userName,String userEmail){
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

}
