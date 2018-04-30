package com.example.santiago.cuantassabes.fragments.pacient;

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
    private List<Category> categoryLists;
    private String pacientAge;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    private String mUsername;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pacient_navigation, container, false);
        ButterKnife.bind(this, rootView);
        categoryLists = new ArrayList<>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("edades").child(pacientAge);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.ic_apps_black_24dp, R.color.color_tab_1);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.ic_maps_local_attraction, R.color.color_tab_2);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_3, R.drawable.ic_maps_local_restaurant, R.color.color_tab_3);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);

        bottomNavigation.setForceTint(true);
        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setColored(true);
        bottomNavigation.setAccentColor(R.color.accent);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        ImagesList profileFragment = new ImagesList();
                        profileFragment.setData("2-4","Animales");
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
        attachDatabaseReadListener();
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String categoria = dataSnapshot.getKey();
                    String categoryUrl = "";
                    Iterator<DataSnapshot> iteratorImages = dataSnapshot.getChildren().iterator();
                    DataSnapshot images = iteratorImages.next();
                    Image imageForUrl = images.getValue(Image.class);
                    if (imageForUrl != null) {
                        categoryUrl = imageForUrl.getPhotoUrl();
                    }
                    Category categoryToAdd = new Category(pacientAge, categoria, categoryUrl);
                    categoryLists.add(categoryToAdd);
                    bottomNavigation.setCurrentItem(0);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
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
