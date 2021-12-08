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
import android.widget.*

class ChangeDistance : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private lateinit var doneButton: Button
    private lateinit var backButton: ImageView
    private lateinit var distance: TextView
    private lateinit var seekBar: SeekBar

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_distance)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData")
        val uid = mAuth.uid
        doneButton = findViewById(R.id.changeDistance)
        distance = findViewById(R.id.totalDistance)
        val userDistance = intent.getStringExtra("CURRENT_DISTANCE")!!
        distance.text = userDistance
        doneButton.setOnClickListener {
            val checker = distance.text.toString()
            if (checker.isEmpty()) {
                val intent = Intent(this, UserProfile::class.java)
                val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
                intent.putExtra("FRAGMENT_SELECTED", fragSelected)
                startActivity(intent)
            } else {
                val distanceNum = distance.text.toString().toLong()
                changeDistance(distanceNum, uid!!)
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
        seekBar = findViewById(R.id.changeDistanceSeekBar)
        seekBar.max = 200
        seekBar.min = 0
        seekBar.progress = userDistance.toInt()
        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            var progressChangedValue = 0
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                progressChangedValue = progress
                distance.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // TODO Auto-generated method stub
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Toast.makeText(
                    this@ChangeDistance, "Seek bar progress is :$progressChangedValue",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun changeDistance(distanceNum: Long, uid: String) {
        mDatabase.child(uid).child("distance").setValue(distanceNum)
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