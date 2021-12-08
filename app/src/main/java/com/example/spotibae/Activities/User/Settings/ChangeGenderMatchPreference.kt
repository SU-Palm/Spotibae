package com.example.spotibae.Activities.User.Settings

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import com.example.spotibae.R
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import com.example.spotibae.Activities.User.UserProfile
import android.view.View
import android.widget.*

class ChangeGenderMatchPreference : AppCompatActivity() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mDatabase: DatabaseReference? = null
    private lateinit var doneButton: Button
    private lateinit var maleButton: Button
    private lateinit var femaleButton: Button
    private lateinit var theyThemButton: Button
    private lateinit var backButton: ImageView
    private lateinit var genderSelected: String
    private lateinit var selectedTheyThem: ImageView
    private lateinit var selectedMale: ImageView
    private lateinit var selectedFemale: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_gender)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData")
        val uid = mAuth.uid
        doneButton = findViewById(R.id.changeGender)
        maleButton = findViewById(R.id.buttonMale)
        femaleButton = findViewById(R.id.buttonFemale)
        theyThemButton = findViewById(R.id.buttonTheyThem)
        doneButton.setOnClickListener {
            if (genderSelected.isEmpty()) {
                val intent = Intent(this, UserProfile::class.java)
                val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
                intent.putExtra("FRAGMENT_SELECTED", fragSelected)
                startActivity(intent)
            } else {
                changeGenderPref(genderSelected, uid!!)
                val intent = Intent(this, UserProfile::class.java)
                val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
                intent.putExtra("FRAGMENT_SELECTED", fragSelected)
                startActivity(intent)
            }
        }
        selectedTheyThem = findViewById(R.id.selectedTheyThem)
        selectedMale = findViewById(R.id.selectedMale)
        selectedFemale = findViewById(R.id.selectedFemale)
        maleButton.setOnClickListener {
            genderSelected = maleButton.text.toString()
            if (selectedTheyThem.isShown) {
                selectedTheyThem.visibility = View.INVISIBLE
            } else if (selectedFemale.isShown) {
                selectedFemale.visibility = View.INVISIBLE
            }
            selectedMale.visibility = View.VISIBLE
        }
        femaleButton.setOnClickListener {
            genderSelected = femaleButton.text.toString()
            if (selectedTheyThem.isShown) {
                selectedTheyThem.visibility = View.INVISIBLE
            } else if (selectedMale.isShown) {
                selectedMale.visibility = View.INVISIBLE
            }
            selectedFemale.visibility = View.VISIBLE
        }
        theyThemButton.setOnClickListener {
            genderSelected = theyThemButton.text.toString()
            if (selectedFemale.isShown) {
                selectedFemale.visibility = View.INVISIBLE
            } else if (selectedMale.isShown) {
                selectedMale.visibility = View.INVISIBLE
            }
            selectedTheyThem.visibility = View.VISIBLE
        }
        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, UserProfile::class.java)
            val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
            intent.putExtra("FRAGMENT_SELECTED", fragSelected)
            startActivity(intent)
        }
    }

    private fun changeGenderPref(genderText: String?, uid: String?) {
        mDatabase!!.child(uid!!).child("genderPref").setValue(genderText)
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