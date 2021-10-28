package com.example.spotibae.Activities.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.spotibae.Activities.Welcome.BaseActivity;
import com.example.spotibae.R;

public class UserProfile extends AppCompatActivity {

    TextView doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        doneButton = findViewById(R.id.done);


        doneButton.setOnClickListener( view -> {
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        });
    }
}