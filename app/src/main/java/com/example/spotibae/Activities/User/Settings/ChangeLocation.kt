package com.example.spotibae.Activities.User.Settings

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import com.example.spotibae.R
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import com.example.spotibae.Activities.User.UserProfile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.widget.*
import java.io.IOException
import java.util.*

class ChangeLocation : AppCompatActivity() {
    // Google maps
    private val REQUEST_LOCATION_PERMISSION = 0
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mDatabase: DatabaseReference? = null
    var uid: String? = null
    private lateinit var doneButton: Button
    private lateinit var setLocationButton: Button
    private lateinit var mlocation: TextView
    private lateinit var backButton: ImageView
    private lateinit var mLastLocation: Location
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_location)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData")
        uid = mAuth.uid
        doneButton = findViewById(R.id.changeLocation)
        setLocationButton = findViewById(R.id.setLocation)
        mlocation = findViewById(R.id.textLocation)
        doneButton.setOnClickListener {
            val intent = Intent(this, UserProfile::class.java)
            val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
            intent.putExtra("FRAGMENT_SELECTED", fragSelected)
            startActivity(intent)
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setLocationButton.setOnClickListener { location() }
        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, UserProfile::class.java)
            val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
            intent.putExtra("FRAGMENT_SELECTED", fragSelected)
            startActivity(intent)
        }
    }

    private fun convertCorToAddress() {
        val addresses: List<Address>
        val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
        try {
            addresses =
                geocoder.getFromLocation(mLastLocation.latitude, mLastLocation.longitude, 1)
            val address = addresses[0].getAddressLine(0)
            val city = addresses[0].locality
            val state = addresses[0].adminArea
            val locationFirebase = "$city, $state"
            mlocation.text = locationFirebase
            changeLocation(locationFirebase, uid!!)
            println("City, State: $locationFirebase")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun location() {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
                )
            } else {
                mFusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        mLastLocation = location
                        convertCorToAddress()
                    } else {
                        mlocation.setText(R.string.no_location)
                    }
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                location()
            } else {
                Toast.makeText(
                    this,
                    R.string.location_permission_denied,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun changeLocation(location: String, uid: String) {
        mDatabase!!.child(uid).child("location").setValue(location)
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