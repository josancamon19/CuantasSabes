package com.example.santiago.cuantassabes.fragments.pacient;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.santiago.cuantassabes.R;
import com.example.santiago.cuantassabes.adapters.RecyclerImagesAdapter;
import com.example.santiago.cuantassabes.model.Image;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagesList extends Fragment implements RecyclerImagesAdapter.OnImageClick {
    @BindView(R.id.recycler_images)
    RecyclerView recyclerView;
    private RecyclerImagesAdapter recyclerImagesAdapter;
    private String age;
    private String category;
    private List<Image> imageList;
    private TextToSpeech textToSpeech;
    //firebase instances
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    public ImagesList() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_images_list, container, false);
        ButterKnife.bind(this, rootView);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("edades").child(age);
        recyclerImagesAdapter = new RecyclerImagesAdapter(getContext(), this);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));
        recyclerView.setAdapter(recyclerImagesAdapter);
        attachDatabaseReadListener();
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        recyclerImagesAdapter.setData(null);
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            imageList = new ArrayList<>();
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Iterator<DataSnapshot> categories = dataSnapshot.getChildren().iterator();
                    while (categories.hasNext()) {
                        DataSnapshot ds = categories.next();
                        Image image = ds.getValue(Image.class);
                        imageList.add(image);
                    }
                    recyclerImagesAdapter.setData(imageList);
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
        }
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    public void setData(String age) {
        this.age = age;
    }

    @Override
    public void setOnImageClick(final Image image) {
        Toast.makeText(getContext(), image.getName(), Toast.LENGTH_SHORT).show();
        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.d("TTS", "This Language is not supported");
                    } else {
                        textToSpeech.speak(image.getName(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }
        });
    }
}
