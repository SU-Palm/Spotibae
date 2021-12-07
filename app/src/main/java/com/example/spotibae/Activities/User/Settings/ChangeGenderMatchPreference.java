package com.example.spotibae.Activities.User.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    ImageView backButton;
    String genderSelected = null;

    ImageView selectedTheyThem;
    ImageView selectedMale;
    ImageView selectedFemale;

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

        doneButton.setOnClickListener( view -> {
            if(genderSelected == null || genderSelected.isEmpty()) {
                Intent intent = new Intent(this, UserProfile.class);
                String fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString();
                intent.putExtra("FRAGMENT_SELECTED", fragSelected);
                startActivity(intent);
            } else {
                changeGenderPref(genderSelected, uid);
                Intent intent = new Intent(this, UserProfile.class);
                String fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString();
                intent.putExtra("FRAGMENT_SELECTED", fragSelected);
                startActivity(intent);
            }
        });

        selectedTheyThem = findViewById(R.id.selectedTheyThem);
        selectedMale = findViewById(R.id.selectedMale);
        selectedFemale = findViewById(R.id.selectedFemale);

        maleButton.setOnClickListener(view -> {
            genderSelected = maleButton.getText().toString();
            if(selectedTheyThem.isShown()) {
                selectedTheyThem.setVisibility(View.INVISIBLE);
            } else if(selectedFemale.isShown()) {
                selectedFemale.setVisibility(View.INVISIBLE);
            }
            selectedMale.setVisibility(View.VISIBLE);
        });

        femaleButton.setOnClickListener(view -> {
            genderSelected = femaleButton.getText().toString();
            if(selectedTheyThem.isShown()) {
                selectedTheyThem.setVisibility(View.INVISIBLE);
            } else if(selectedMale.isShown()) {
                selectedMale.setVisibility(View.INVISIBLE);
            }
            selectedFemale.setVisibility(View.VISIBLE);
        });

        theyThemButton.setOnClickListener(view -> {
            genderSelected = theyThemButton.getText().toString();
            if(selectedFemale.isShown()) {
                selectedFemale.setVisibility(View.INVISIBLE);
            } else if(selectedMale.isShown()) {
                selectedMale.setVisibility(View.INVISIBLE);
            }
            selectedTheyThem.setVisibility(View.VISIBLE);
        });

        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener( view -> {
            Intent intent = new Intent(this, UserProfile.class);
            String fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString();
            intent.putExtra("FRAGMENT_SELECTED", fragSelected);
            startActivity(intent);
        });
    }

    public void changeGenderPref(String genderText, String uid) {
        mDatabase.child(uid).child("genderPref").setValue(genderText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
}