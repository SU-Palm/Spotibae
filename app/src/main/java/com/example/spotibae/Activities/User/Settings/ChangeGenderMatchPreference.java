package com.example.spotibae.Activities.User.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.example.spotibae.Activities.User.UserProfile;
import com.example.spotibae.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeGenderMatchPreference extends AppCompatActivity {
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
        setContentView(R.layout.activity_change_gender_match_preference);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData");
        String uid = mAuth.getUid();

        doneButton = findViewById(R.id.changeGender);
        maleButton = findViewById(R.id.buttonMale);
        femaleButton = findViewById(R.id.buttonFemale);
        theyThemButton = findViewById(R.id.buttonTheyThem);

        maleButton.setOnClickListener(view -> {
            mDatabase.child(uid).child("genderPref").setValue("Male");
        });

        femaleButton.setOnClickListener(view -> {
            mDatabase.child(uid).child("genderPref").setValue("Female");
        });

        theyThemButton.setOnClickListener(view -> {
            mDatabase.child(uid).child("genderPref").setValue("They/Them");
        });

        doneButton.setOnClickListener( view -> {
            Intent intent = new Intent(this, UserProfile.class);
            startActivity(intent);
        });
    }
}