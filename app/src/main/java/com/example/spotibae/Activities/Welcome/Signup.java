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

import com.example.spotibae.Activities.Matching.Matching;
import com.example.spotibae.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText userEmail;
    private EditText userPswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("");  // Removes name from action bar

        userEmail = findViewById(R.id.editTextTextEmailAddress);
        userPswd = findViewById(R.id.editTextTextPassword);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        TextView loginTextViewButton = findViewById(R.id.loginTextButton);
        Button signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(v -> createAccount(userEmail.getText().toString(), userPswd.getText().toString()));
        loginTextViewButton.setOnClickListener(v -> login());
    }

    private void createAccount(String email, String password){
        //System.out.println("Email: " + email + " Password:" + password);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        System.out.println("Email: " + email + " Password:" + password);
                        if (task.isSuccessful()) {
                            System.out.println("Email: " + email + " Password:" + password);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            signUp();
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

    private void signUp() {
        Intent intent = new Intent(this, Matching.class);
        startActivity(intent);
    }
}