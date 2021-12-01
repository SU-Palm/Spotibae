package com.example.spotibae.Activities.User.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.spotibae.Activities.User.UserProfile;
import com.example.spotibae.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeName extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Button doneButton;
    ImageView backButton;
    EditText firstName;
    EditText lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData");
        String uid = mAuth.getUid();

        doneButton = findViewById(R.id.changeName);
        firstName = findViewById(R.id.editTextFirstName);
        lastName = findViewById(R.id.editTextLastName);
        doneButton.setOnClickListener( view -> {
            String nameText = firstName.getText().toString().concat(" ").concat(lastName.getText().toString());
            boolean checker = checkForNumbers(nameText);
            if(checker && !nameText.equals(" ")) {
                changeName(nameText, uid);
                Intent intent = new Intent(this, UserProfile.class);
                String fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString();
                intent.putExtra("FRAGMENT_SELECTED", fragSelected);
                startActivity(intent);
            } else {
                Toast.makeText(ChangeName.this, "Invalid Name Inputted",
                        Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(this, UserProfile.class);
                //startActivity(intent);
            }
        });

        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener( view -> {
            Intent intent = new Intent(this, UserProfile.class);
            String fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString();
            intent.putExtra("FRAGMENT_SELECTED", fragSelected);
            startActivity(intent);
        });
    }

    public boolean checkForNumbers(String sample) {
        char[] chars = sample.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char c : chars){
            if(Character.isDigit(c)){
                sb.append(c);
            }
        }

        if(sb.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void changeName(String nameText, String uid) {
        String firstName = nameText.substring(0, nameText.indexOf(" "));
        String lastName = nameText.substring(nameText.indexOf(" ") + 1);
        mDatabase.child(uid).child("firstName").setValue(firstName);
        mDatabase.child(uid).child("lastName").setValue(lastName);
        mDatabase.child(uid).child("fullName").setValue(nameText);
    }
}