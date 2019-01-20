package com.example.santiago.cuantassabes.fragments.pacient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.santiago.cuantassabes.QuizActivity;
import com.example.santiago.cuantassabes.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizIntroduction extends Fragment {
    @BindView(R.id.button_start_quiz)
    Button buttonStartQuiz;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_quiz_introduction,container,false);
        ButterKnife.bind(this,rootView);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QuizActivity.class);
                intent.putExtra("age","2-4-01");
                startActivity(intent);
            }
        });
        return rootView;
    }
}
