package com.example.spotibae.Activities.Welcome

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import com.example.spotibae.R
import android.content.Intent
import android.view.View
import android.widget.Button

class WelcomeScreen : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        setListeners()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        if (currentUser != null) {
            skipToHome()
        }
    }

    private fun setListeners() {
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        loginButton.setOnClickListener { login() }
        signUpButton.setOnClickListener { signUp() }
    }

    private fun login() {
        val intent = Intent(this, Login::class.java)
        this.startActivity(intent)
    }

    private fun signUp() {
        val intent = Intent(this, Signup::class.java)
        this.startActivity(intent)
    }

    private fun skipToHome() {
        val intent = Intent(this, BaseActivity::class.java)
        this.startActivity(intent)
    }
}