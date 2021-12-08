package com.example.spotibae.Activities.User.Settings

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import com.example.spotibae.R
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import com.example.spotibae.Activities.User.UserProfile
import androidx.annotation.RequiresApi
import android.os.Build
import android.widget.SeekBar.OnSeekBarChangeListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.view.View
import android.widget.*
import com.example.spotibae.Activities.User.Settings.ChangeLocation
import com.google.android.gms.tasks.OnSuccessListener

class ChangeGenderMatchPreference : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null
    var doneButton: Button? = null
    var maleButton: Button? = null
    var femaleButton: Button? = null
    var theyThemButton: Button? = null
    var backButton: ImageView? = null
    var genderSelected: String? = null
    var selectedTheyThem: ImageView? = null
    var selectedMale: ImageView? = null
    var selectedFemale: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_gender_match_preference)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData")
        val uid = mAuth!!.uid
        doneButton = findViewById(R.id.changeGender)
        maleButton = findViewById(R.id.buttonMale)
        femaleButton = findViewById(R.id.buttonFemale)
        theyThemButton = findViewById(R.id.buttonTheyThem)
        doneButton?.setOnClickListener(View.OnClickListener { view: View? ->
            if (genderSelected == null || genderSelected!!.isEmpty()) {
                val intent = Intent(this, UserProfile::class.java)
                val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
                intent.putExtra("FRAGMENT_SELECTED", fragSelected)
                startActivity(intent)
            } else {
                changeGenderPref(genderSelected, uid)
                val intent = Intent(this, UserProfile::class.java)
                val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
                intent.putExtra("FRAGMENT_SELECTED", fragSelected)
                startActivity(intent)
            }
        })
        selectedTheyThem = findViewById(R.id.selectedTheyThem)
        selectedMale = findViewById(R.id.selectedMale)
        selectedFemale = findViewById(R.id.selectedFemale)
        maleButton?.setOnClickListener(View.OnClickListener { view: View? ->
            genderSelected = maleButton?.getText().toString()
            if (selectedTheyThem?.isShown() == true) {
                selectedTheyThem?.setVisibility(View.INVISIBLE)
            } else if (selectedFemale?.isShown() == true) {
                selectedFemale?.setVisibility(View.INVISIBLE)
            }
            selectedMale?.setVisibility(View.VISIBLE)
        })
        femaleButton?.setOnClickListener(View.OnClickListener { view: View? ->
            genderSelected = femaleButton?.getText().toString()
            if (selectedTheyThem?.isShown() == true) {
                selectedTheyThem?.setVisibility(View.INVISIBLE)
            } else if (selectedMale?.isShown() == true) {
                selectedMale?.setVisibility(View.INVISIBLE)
            }
            selectedFemale?.setVisibility(View.VISIBLE)
        })
        theyThemButton?.setOnClickListener(View.OnClickListener { view: View? ->
            genderSelected = theyThemButton?.getText().toString()
            if (selectedFemale?.isShown == true) {
                selectedFemale?.visibility = View.INVISIBLE
            } else if (selectedMale?.isShown == true) {
                selectedMale?.visibility = View.INVISIBLE
            }
            selectedTheyThem?.visibility = View.VISIBLE
        })
        backButton = findViewById(R.id.backButton)
        backButton?.setOnClickListener(View.OnClickListener { view: View? ->
            val intent = Intent(this, UserProfile::class.java)
            val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
            intent.putExtra("FRAGMENT_SELECTED", fragSelected)
            startActivity(intent)
        })
    }

    fun changeGenderPref(genderText: String?, uid: String?) {
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