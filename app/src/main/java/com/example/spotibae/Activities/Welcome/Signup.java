package com.example.spotibae.Activities.Welcome;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotibae.Models.User;
import com.example.spotibae.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText userEmail;
    private EditText userPswd;
    private EditText userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("");  // Removes name from action bar

        userEmail = findViewById(R.id.editTextTextEmailAddress);
        userPswd = findViewById(R.id.editTextTextPassword);
        userName = findViewById(R.id.editTextTextFullName);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        TextView loginTextViewButton = findViewById(R.id.loginTextButton);
        Button signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(v -> createAccount(userEmail.getText().toString(), userPswd.getText().toString(), userName.getText().toString()));
        loginTextViewButton.setOnClickListener(v -> login());
    }

    private void createAccount(String email, String password, String fullname) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            User userData = new User(email, fullname);
                            
                            FirebaseDatabase.getInstance().getReference("UserData")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userData).
                                    addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            //    progressbar GONE
                                            // signUp_progress.setVisibility(View.GONE);

                                            Toast.makeText(Signup.this, "Successful Registered", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Signup.this, BaseActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void login() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}