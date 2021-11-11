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

public class ChangeName extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Button doneButton;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData");
        String uid = mAuth.getUid();

        doneButton = findViewById(R.id.changeName);
        name = findViewById(R.id.editTextName);
        doneButton.setOnClickListener( view -> {
            String nameText = name.getText().toString();
            if(nameText.isEmpty()) {
                Intent intent = new Intent(this, UserProfile.class);
                startActivity(intent);
            } else {
                changeName(nameText, uid);

                Intent intent = new Intent(this, UserProfile.class);
                startActivity(intent);
            }
        });
    }

    public void changeName(String nameText, String uid) {
        String firstName = nameText.substring(0, nameText.indexOf(" "));
        String lastName = nameText.substring(nameText.indexOf(" ") + 1);
        mDatabase.child(uid).child("firstName").setValue(firstName);
        mDatabase.child(uid).child("lastName").setValue(lastName);
        mDatabase.child(uid).child("fullName").setValue(nameText);
    }
}