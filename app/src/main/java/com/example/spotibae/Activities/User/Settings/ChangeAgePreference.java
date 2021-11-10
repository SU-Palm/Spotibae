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

public class ChangeAgePreference extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Button doneButton;
    EditText lowestAgePref;
    EditText highestAgePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_age_preference);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData");
        String uid = mAuth.getUid();

        doneButton = findViewById(R.id.changeAgePreference);
        lowestAgePref = findViewById(R.id.editTextLowestAge);
        highestAgePref = findViewById(R.id.editTextHighestAge);
        doneButton.setOnClickListener( view -> {
            String lowestAgeText = lowestAgePref.getText().toString();
            String highestAgeText = highestAgePref.getText().toString();

            if(lowestAgeText.isEmpty() || highestAgeText.isEmpty()) {
                Intent intent = new Intent(this, UserProfile.class);
                startActivity(intent);
            } else {
                long lowestAgeNum = Long.parseLong(lowestAgeText);
                long highestAgeNum = Long.parseLong(highestAgeText);
                changeAgePref(lowestAgeNum, highestAgeNum, uid);

                Intent intent = new Intent(this, UserProfile.class);
                startActivity(intent);
            }
        });
    }

    public void changeAgePref(long lowestAgeNum, long highestAgeNum, String uid) {
        mDatabase.child(uid).child("lowestAgePref").setValue(lowestAgeNum);
        mDatabase.child(uid).child("highestAgePref").setValue(highestAgeNum);
    }
}