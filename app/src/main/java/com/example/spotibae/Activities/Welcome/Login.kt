package com.example.spotibae.Activities.Welcome

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import com.example.spotibae.R
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.helper.widget.MotionEffect
import android.widget.Toast

class Login : AppCompatActivity() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var userEmail: EditText
    private lateinit var userPswd: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.title = ""
        val signUpTextViewButton = findViewById<TextView>(R.id.signUpTextButton)
        val loginButton = findViewById<Button>(R.id.loginButton)
        userEmail = findViewById(R.id.editTextTextEmailAddress)
        userPswd = findViewById(R.id.editTextTextPassword)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        loginButton.setOnClickListener { signInToFirebase(userEmail.text.toString(), userPswd.text.toString()) }
        signUpTextViewButton.setOnClickListener { signUp() }
    }

    private fun signInToFirebase(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(MotionEffect.TAG, "signInWithEmail:success")
                    val user = mAuth.currentUser
                    login()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(MotionEffect.TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@Login, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun login() {
        val intent = Intent(this, BaseActivity::class.java)
        intent.putExtra("FRAGMENT_SELECTED", "Dashboard")
        startActivity(intent)
    }

    private fun signUp() {
        val intent = Intent(this, Signup::class.java)
        startActivity(intent)
    }
}