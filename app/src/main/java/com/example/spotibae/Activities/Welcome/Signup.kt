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
import com.example.spotibae.Models.User

class Signup : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var userEmail: EditText? = null
    private var userPswd: EditText? = null
    private var userName: EditText? = null
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
        signUpButton.setOnClickListener { v: View? ->
            createAccount(
                userEmail?.getText().toString(),
                userPswd?.getText().toString(),
                userName?.getText().toString()
            )
        }
        loginTextViewButton.setOnClickListener { v: View? -> login() }
    }

    private fun createAccount(email: String, password: String, fullname: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(MotionEffect.TAG, "createUserWithEmail:success")
                    val user = mAuth!!.currentUser
                    val userData = User(email, fullname)
                    FirebaseDatabase.getInstance().getReference("UserData")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(userData)
                        .addOnCompleteListener { //    progressbar GONE
                            // signUp_progress.setVisibility(View.GONE);
                            val user = mAuth!!.currentUser
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