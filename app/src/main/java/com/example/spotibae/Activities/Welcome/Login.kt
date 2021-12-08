package com.example.spotibae.Activities.Welcome

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import com.example.spotibae.Activities.Welcome.BaseActivity
import com.example.spotibae.R
import com.example.spotibae.Fragments.UserFrag
import com.example.spotibae.Fragments.MessagesFrag
import com.example.spotibae.Fragments.QRFrag
import android.content.Intent
import com.example.spotibae.Activities.User.UserProfile
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.squareup.picasso.Picasso
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.OnFailureListener
import android.graphics.Bitmap
import android.graphics.PorterDuffXfermode
import android.graphics.PorterDuff
import android.graphics.BitmapShader
import android.graphics.Shader
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.AuthResult
import androidx.constraintlayout.helper.widget.MotionEffect
import android.widget.Toast
import com.example.spotibae.Activities.Welcome.Signup
import com.example.spotibae.Activities.Welcome.Login

class Login : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var userEmail: EditText? = null
    private var userPswd: EditText? = null
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
        loginButton.setOnClickListener { v: View? ->
            signInToFirebase(
                userEmail?.getText().toString(), userPswd?.getText().toString()
            )
        }
        signUpTextViewButton.setOnClickListener { v: View? -> signUp() }
    }

    private fun signInToFirebase(email: String, password: String) {
        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(MotionEffect.TAG, "signInWithEmail:success")
                    val user = mAuth!!.currentUser
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