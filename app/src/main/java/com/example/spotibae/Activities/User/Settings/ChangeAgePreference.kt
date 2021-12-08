package com.example.spotibae.Activities.User.Settings

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import com.example.spotibae.R
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import com.example.spotibae.Activities.User.UserProfile
import android.widget.*

class ChangeAgePreference : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var doneButton: Button
    private lateinit var backButton: ImageView
    private lateinit var lowestAgePref: EditText
    private lateinit var highestAgePref: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_age_preference)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData")
        val uid = mAuth.uid
        doneButton = findViewById(R.id.changeAgePreference)
        lowestAgePref = findViewById(R.id.editTextLowestAge)
        highestAgePref = findViewById(R.id.editTextHighestAge)
        doneButton.setOnClickListener {
            val lowestAgeText = lowestAgePref.text.toString()
            val highestAgeText = highestAgePref.text.toString()
            if (lowestAgeText.isEmpty() || highestAgeText.isEmpty()) {
                val intent = Intent(this, UserProfile::class.java)
                val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
                intent.putExtra("FRAGMENT_SELECTED", fragSelected)
                startActivity(intent)
            } else {
                val lowestAgeNum = lowestAgeText.toLong()
                val highestAgeNum = highestAgeText.toLong()
                changeAgePref(lowestAgeNum, highestAgeNum, uid!!)
                val intent = Intent(this, UserProfile::class.java)
                val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
                intent.putExtra("FRAGMENT_SELECTED", fragSelected)
                startActivity(intent)
            }
        }
        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, UserProfile::class.java)
            val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
            intent.putExtra("FRAGMENT_SELECTED", fragSelected)
            startActivity(intent)
        }
    }

    private fun changeAgePref(lowestAgeNum: Long, highestAgeNum: Long, uid: String) {
        mDatabase.child(uid).child("lowestAgePref").setValue(lowestAgeNum)
        mDatabase.child(uid).child("highestAgePref").setValue(highestAgeNum)
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }
}