package com.example.santiago.cuantassabes.fragments.pacient;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.santiago.cuantassabes.R;
import com.example.santiago.cuantassabes.model.Category;
import com.example.santiago.cuantassabes.model.Image;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PacientNavigation extends Fragment {
    private static final String TAG = PacientNavigation.class.getSimpleName();
    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;
    private String pacientAge;


    private String mUsername;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pacient_navigation, container, false);
        ButterKnife.bind(this, rootView);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.ic_practice, R.color.color_tab_1);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.ic_quiz, R.color.color_tab_2);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.ic_user, R.color.color_tab_3);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

        bottomNavigation.setForceTint(true);
        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setColored(true);
        bottomNavigation.setAccentColor(Color.parseColor("#FFC107"));
        bottomNavigation.setInactiveColor(Color.parseColor("#FFFFFF"));
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        ImagesList profileFragment = new ImagesList();
                        profileFragment.setData("2-4");
                        getFragmentManager().beginTransaction().replace(R.id.container_fragments, profileFragment).commit();
                        break;
                    case 1:
                        QuizIntroduction fragmentQuiz = new QuizIntroduction();
                        //fragmentQuiz.setData(pacientAge);
                        getFragmentManager().beginTransaction().replace(R.id.container_fragments, fragmentQuiz).commit();
                        break;
                    case 2:
                        Edit fragmentEdit = new Edit();
                        fragmentEdit.setUser(mUsername);
                        getFragmentManager().beginTransaction().replace(R.id.container_fragments, fragmentEdit).commit();
                        break;
                }
                return true;
            }
        });
        bottomNavigation.setCurrentItem(0);
        return rootView;
    }


    public void setAge(String pacientAge) {
        int age = Integer.valueOf(pacientAge);
        if (age >= 2 && age < 4) {
            this.pacientAge = "2-4";
        } else if (age >= 4 && age < 6) {
            this.pacientAge = "4-6";
        } else if (age >= 6 && age < 8) {
            this.pacientAge = "6-8";
        }
    }
    public void setUser(String username){
        this.mUsername = username;
    }

}
