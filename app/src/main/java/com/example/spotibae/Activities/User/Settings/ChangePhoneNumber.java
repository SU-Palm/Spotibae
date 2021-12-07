package com.example.spotibae.Activities.User.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.spotibae.Activities.User.UserProfile;
import com.example.spotibae.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePhoneNumber extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Button doneButton;
    ImageView backButton;
    EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone_number);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData");
        String uid = mAuth.getUid();

        doneButton = findViewById(R.id.changePhoneNumber);
        phoneNumber = findViewById(R.id.editTextPhone);
        doneButton.setOnClickListener( view -> {
            String phoneNumberText = phoneNumber.getText().toString();
            if(phoneNumberText.isEmpty()) {
                Intent intent = new Intent(this, UserProfile.class);
                String fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString();
                intent.putExtra("FRAGMENT_SELECTED", fragSelected);
                startActivity(intent);
            } else {
                changePhoneNumber(phoneNumberText, uid);
                Intent intent = new Intent(this, UserProfile.class);
                String fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString();
                intent.putExtra("FRAGMENT_SELECTED", fragSelected);
                startActivity(intent);
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

    public void changePhoneNumber(String phoneNumber, String uid) {
        mDatabase.child(uid).child("phoneNumber").setValue(phoneNumber);
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
