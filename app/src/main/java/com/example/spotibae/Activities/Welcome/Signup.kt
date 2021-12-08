package com.example.spotibae.Activities.Welcome

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import com.example.spotibae.R
import android.content.Intent
import com.google.firebase.database.FirebaseDatabase
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.helper.widget.MotionEffect
import android.widget.Toast
import com.example.spotibae.Models.User

class Signup : AppCompatActivity() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var userEmail: EditText
    private lateinit var userPswd: EditText
    private lateinit var userName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar!!.title = "" // Removes name from action bar
        userEmail = findViewById(R.id.editTextTextEmailAddress)
        userPswd = findViewById(R.id.editTextTextPassword)
        userName = findViewById(R.id.editTextTextFullName)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        val loginTextViewButton = findViewById<TextView>(R.id.loginTextButton)
        val signUpButton = findViewById<Button>(R.id.signUpButton)

        signUpButton.setOnClickListener { createAccount(userEmail.text.toString(), userPswd.text.toString(), userName.text.toString()) }

        loginTextViewButton.setOnClickListener { login() }
    }

    private fun createAccount(email: String, password: String, fullName: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(MotionEffect.TAG, "createUserWithEmail:success")
                    val user = mAuth.currentUser
                    val userData = User(email, fullName)
                    FirebaseDatabase.getInstance().getReference("UserData")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(userData)
                        .addOnCompleteListener { //    progressbar GONE
                            // signUp_progress.setVisibility(View.GONE);
                            val user = mAuth.currentUser
                            val uId = user!!.uid
                            // FirebaseDatabase.getInstance().getReference("UserData").child(uId).child("matches").child(" ").setValue(true);
                            FirebaseDatabase.getInstance().getReference("UserData").child(uId)
                                .child("firstLogin").setValue(true)
                            Toast.makeText(this@Signup, "Successful Registered", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(this@Signup, BaseActivity::class.java)
                            intent.putExtra("FRAGMENT_SELECTED", "Dashboard")
                            startActivity(intent)
                            finish()
                        }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(MotionEffect.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@Signup, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun login() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}