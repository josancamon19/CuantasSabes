package com.example.santiago.cuantassabes.fragments.pacient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.santiago.cuantassabes.PacientActivity;
import com.example.santiago.cuantassabes.R;
import com.example.santiago.cuantassabes.model.Image;
import com.example.santiago.cuantassabes.ui.MyImageSwitcher;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class Quiz extends Fragment {

    //xml references
    @BindView(R.id.image_switcher)
    MyImageSwitcher imageSwitcher;
    @BindView(R.id.ic_mic)
    ImageView mic;

    //code variables
    private String age;
    private List<Image> imageList;
    private List<Image> imagesQuiz;
    private int imageSwitcherIndex = 0;
    private final int CODE = 1;
    private int score = 0;

    //Firebase Instances
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);
        ButterKnife.bind(this, rootView);
        imageSwitcherIndex++;
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getContext());
                return imageView;
            }
        });
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("edades").child(age);
        imageList = new ArrayList<>();
        imagesQuiz = new ArrayList<>();

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(intent, CODE);
                } else {
                    Toast.makeText(getContext(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
                }
            }
        });
        attachDatabaseReadListener();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE:
                if (resultCode == RESULT_OK && data != null) {
                    if (imagesQuiz != null && !imagesQuiz.isEmpty()) {
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        String word_listened = unaccent(result.get(0).trim().toLowerCase());
                        List<String> correct_word = imagesQuiz.get(imageSwitcherIndex - 1).getAnswers();
                        if (correct_word.contains(word_listened)) {
                            Toast.makeText(getContext(), "respuesta correcta", Toast.LENGTH_SHORT).show();
                            score++;
                        } else {
                            Toast.makeText(getContext(), "dijiste = " + word_listened + " \nincorrecta= " + correct_word, Toast.LENGTH_LONG).show();
                        }
                        if (imageSwitcherIndex < imagesQuiz.size()) {
                            imageSwitcherIndex++;
                            imageSwitcher.setImageUrl(imagesQuiz.get(imageSwitcherIndex - 1).getPhotoUrl());
                        } else {
                            Toast.makeText(getContext(), "Tu puntaje fue " + score + "/" + imagesQuiz.size(), Toast.LENGTH_SHORT).show();
                            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                                    .setCancelable(false)
                                    .setMessage("Felicidades tu puntaje fue = "+ score + " de "+ imagesQuiz.size()).
                                            setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(getContext(), PacientActivity.class);
                                                    intent.putExtra("age","3");
                                                    startActivity(intent);
                                                }
                                            }).create();
                            alertDialog.show();
                        }
                    }
                }
                break;
        }
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
                    Iterator<DataSnapshot> imagesAvailable = dataSnapshot.getChildren().iterator();
                    while (imagesAvailable.hasNext()) {
                        DataSnapshot imageAvailable = imagesAvailable.next();
                        Image image = imageAvailable.getValue(Image.class);
                        imageList.add(image);
                    }
                    Random random = new Random();
                    int imagesForQuiz;
                    if (imageList.size()<2){
                        imagesForQuiz = 1;
                    }else {
                        imagesForQuiz = imageList.size()/2;
                    }
                    for (int i = 0; i < imagesForQuiz; i++) {
                        int randomIndex = random.nextInt(imageList.size());
                        imagesQuiz.add(imageList.get(randomIndex));
                        imageList.remove(randomIndex);
                    }
                    imageSwitcher.setImageUrl(imagesQuiz.get(imageSwitcherIndex - 1).getPhotoUrl());
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
        this.age = "2-4";
    }

    public static String unaccent(String stringReceived) {
        return Normalizer
                .normalize(stringReceived, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        //removes the accents in a string
    }
}
