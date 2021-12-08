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
import java.lang.StringBuilder

class ChangeName : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null
    var doneButton: Button? = null
    var backButton: ImageView? = null
    var firstName: EditText? = null
    var lastName: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_name)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData")
        val uid = mAuth!!.uid
        doneButton = findViewById<Button>(R.id.changeName)
        firstName = findViewById<EditText>(R.id.editTextFirstName)
        lastName = findViewById(R.id.editTextLastName)
        doneButton?.setOnClickListener(View.OnClickListener { view: View? ->
            val nameText = firstName?.getText().toString() + " " + lastName?.getText().toString()
            val checker = checkForNumbers(nameText)
            if (checker && nameText != " ") {
                changeName(nameText, uid)
                val intent = Intent(this, UserProfile::class.java)
                val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
                intent.putExtra("FRAGMENT_SELECTED", fragSelected)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this@ChangeName, "Invalid Name Inputted",
                    Toast.LENGTH_SHORT
                ).show()
                //Intent intent = new Intent(this, UserProfile.class);
                //startActivity(intent);
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

    fun checkForNumbers(sample: String): Boolean {
        val chars = sample.toCharArray()
        val sb = StringBuilder()
        for (c in chars) {
            if (Character.isDigit(c)) {
                sb.append(c)
            }
        }
        return if (sb.length == 0) {
            true
        } else {
            false
        }
    }

    fun changeName(nameText: String, uid: String?) {
        val firstName = nameText.substring(0, nameText.indexOf(" "))
        val lastName = nameText.substring(nameText.indexOf(" ") + 1)
        mDatabase!!.child(uid!!).child("firstName").setValue(firstName)
        mDatabase!!.child(uid).child("lastName").setValue(lastName)
        mDatabase!!.child(uid).child("fullName").setValue(nameText)
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