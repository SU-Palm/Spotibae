package com.example.spotibae.Activities.User.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.spotibae.Activities.User.UserProfile;
import com.example.spotibae.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeGender extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Button doneButton;
    Button maleButton;
    Button femaleButton;
    Button theyThemButton;
    String genderSelected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_gender);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData");
        String uid = mAuth.getUid();

        doneButton = findViewById(R.id.changeGender);
        maleButton = findViewById(R.id.buttonMale);
        femaleButton = findViewById(R.id.buttonFemale);
        theyThemButton = findViewById(R.id.buttonTheyThem);
        doneButton.setOnClickListener( view -> {
            if(genderSelected.isEmpty() || genderSelected == null) {
                Intent intent = new Intent(this, UserProfile.class);
                startActivity(intent);
            } else {
                changeGender(genderSelected, uid);
                Intent intent = new Intent(this, UserProfile.class);
                startActivity(intent);
            }
        });
        maleButton.setOnClickListener(view -> {
            genderSelected = maleButton.getText().toString();
        });
        femaleButton.setOnClickListener(view -> {
            genderSelected = femaleButton.getText().toString();
        });
        theyThemButton.setOnClickListener(view -> {
            genderSelected = theyThemButton.getText().toString();
        });
    }

    public void changeGender(String genderText, String uid) {
        mDatabase.child(uid).child("gender").setValue(genderText);
    }
}