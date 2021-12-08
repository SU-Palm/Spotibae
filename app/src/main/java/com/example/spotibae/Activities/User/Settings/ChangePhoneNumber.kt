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

class ChangePhoneNumber : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null
    var doneButton: Button? = null
    var backButton: ImageView? = null
    var phoneNumber: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_phone_number)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData")
        val uid = mAuth!!.uid

        doneButton = findViewById(R.id.changePhoneNumber)
        phoneNumber = findViewById(R.id.editTextPhone)

        doneButton?.setOnClickListener(View.OnClickListener { view: View? ->
            val phoneNumberText = phoneNumber?.getText().toString()
            if (phoneNumberText.isEmpty()) {
                val intent = Intent(this, UserProfile::class.java)
                val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
                intent.putExtra("FRAGMENT_SELECTED", fragSelected)
                startActivity(intent)
            } else {
                changePhoneNumber(phoneNumberText, uid)
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

    fun changePhoneNumber(phoneNumber: String?, uid: String?) {
        mDatabase!!.child(uid!!).child("phoneNumber").setValue(phoneNumber)
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