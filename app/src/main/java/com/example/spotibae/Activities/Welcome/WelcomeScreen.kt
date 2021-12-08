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
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.AuthResult
import androidx.constraintlayout.helper.widget.MotionEffect
import android.widget.Toast
import com.example.spotibae.Activities.Welcome.Signup
import com.example.spotibae.Activities.Welcome.Login

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
        loginButton.setOnClickListener { v: View? -> login() }
        signUpButton.setOnClickListener { v: View? -> signUp() }
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