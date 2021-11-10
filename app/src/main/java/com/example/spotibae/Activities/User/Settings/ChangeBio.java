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

public class ChangeBio extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Button doneButton;
    EditText bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_bio);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData");
        String uid = mAuth.getUid();

        doneButton = findViewById(R.id.changeBio);
        bio = findViewById(R.id.editTextBio);
        doneButton.setOnClickListener( view -> {
            String bioText = bio.getText().toString();
            if(bioText.isEmpty()) {
                Intent intent = new Intent(this, UserProfile.class);
                startActivity(intent);
            } else {
                changeBio(bioText, uid);

                Intent intent = new Intent(this, UserProfile.class);
                startActivity(intent);
            }
        });
    }

    public void changeBio(String bioText, String uid) {
        mDatabase.child(uid).child("bio").setValue(bioText);
    }
}