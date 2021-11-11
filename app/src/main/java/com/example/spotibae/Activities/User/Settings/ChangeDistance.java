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

public class ChangeDistance extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Button doneButton;
    EditText distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_distance);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData");
        String uid = mAuth.getUid();

        doneButton = findViewById(R.id.changeDistance);
        distance = findViewById(R.id.editTextDistance);
        doneButton.setOnClickListener( view -> {
            String checker = distance.getText().toString();
            if(checker.isEmpty()) {
                Intent intent = new Intent(this, UserProfile.class);
                startActivity(intent);
            } else {
                long distanceNum = Long.parseLong(distance.getText().toString());
                changeDistance(distanceNum, uid);

                Intent intent = new Intent(this, UserProfile.class);
                startActivity(intent);
            }
        });
    }

    public void changeDistance(long distanceNum, String uid) {
        mDatabase.child(uid).child("distance").setValue(distanceNum);
    }
}