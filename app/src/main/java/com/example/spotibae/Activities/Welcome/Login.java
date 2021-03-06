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

import com.example.spotibae.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText userEmail;
    private EditText userPswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("");

        TextView signUpTextViewButton = findViewById(R.id.signUpTextButton);
        Button loginButton = findViewById(R.id.loginButton);

        userEmail = findViewById(R.id.editTextTextEmailAddress);
        userPswd = findViewById(R.id.editTextTextPassword);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(v -> signInToFirebase(userEmail.getText().toString(), userPswd.getText().toString()));
        signUpTextViewButton.setOnClickListener(v -> signUp());
    }

    private void signInToFirebase(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            login();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void login() {
        Intent intent = new Intent(this, BaseActivity.class);
        intent.putExtra("FRAGMENT_SELECTED", "Dashboard");
        startActivity(intent);
    }

    private void signUp() {
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }
}