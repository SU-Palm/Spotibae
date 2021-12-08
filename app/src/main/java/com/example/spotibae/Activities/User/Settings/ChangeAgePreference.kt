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

class ChangeAgePreference : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null
    var doneButton: Button? = null
    var backButton: ImageView? = null
    var lowestAgePref: EditText? = null
    var highestAgePref: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_age_preference)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData")
        val uid = mAuth!!.uid
        doneButton = findViewById(R.id.changeAgePreference)
        lowestAgePref = findViewById(R.id.editTextLowestAge)
        highestAgePref = findViewById(R.id.editTextHighestAge)
        doneButton?.setOnClickListener(View.OnClickListener { view: View? ->
            val lowestAgeText = lowestAgePref?.getText().toString()
            val highestAgeText = highestAgePref?.getText().toString()
            if (lowestAgeText.isEmpty() || highestAgeText.isEmpty()) {
                val intent = Intent(this, UserProfile::class.java)
                val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
                intent.putExtra("FRAGMENT_SELECTED", fragSelected)
                startActivity(intent)
            } else {
                val lowestAgeNum = lowestAgeText.toLong()
                val highestAgeNum = highestAgeText.toLong()
                changeAgePref(lowestAgeNum, highestAgeNum, uid)
                val intent = Intent(this, UserProfile::class.java)
                val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
                intent.putExtra("FRAGMENT_SELECTED", fragSelected)
                startActivity(intent)
            }
        })
        backButton = findViewById(R.id.backButton)
        backButton?.setOnClickListener(View.OnClickListener { view: View? ->
            val intent = Intent(this, UserProfile::class.java)
            val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
            intent.putExtra("FRAGMENT_SELECTED", fragSelected)
            startActivity(intent)
        })
    }

    fun changeAgePref(lowestAgeNum: Long, highestAgeNum: Long, uid: String?) {
        mDatabase!!.child(uid!!).child("lowestAgePref").setValue(lowestAgeNum)
        mDatabase!!.child(uid).child("highestAgePref").setValue(highestAgeNum)
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