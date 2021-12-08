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
import android.graphics.*
import com.example.spotibae.Activities.User.UserProfile
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.squareup.picasso.Picasso
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.OnFailureListener
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.AuthResult
import androidx.constraintlayout.helper.widget.MotionEffect
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.spotibae.Activities.Welcome.Signup
import com.example.spotibae.Activities.Welcome.Login
import com.squareup.picasso.Transformation
import java.util.HashMap

class BaseActivity : AppCompatActivity() {
    val fm = supportFragmentManager
    var profileImageButton: ImageView? = null
    var btm_nav: BottomNavigationView? = null
    var storage = FirebaseStorage.getInstance()
    var storageRef: StorageReference? = null
    var profileRef: StorageReference? = null
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null
    var fragSelected: String? = null
    var `var`: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        `var` =
            if (isNotNullOrEmpty(intent.getStringExtra("PROFILE_VAR"))) intent.getStringExtra("PROFILE_VAR") else null
        setContentView(R.layout.activity_base)
        btm_nav = findViewById(R.id.bottom_navigation)
        profileImageButton = findViewById(R.id.profilePic)
        println("FRAGMENT_SELECTED In User Profile onCreate() Before: $fragSelected")
        fragSelected =
            if (isNotNullOrEmpty(intent.getStringExtra("FRAGMENT_SELECTED"))) intent.getStringExtra(
                "FRAGMENT_SELECTED"
            ) else "Dashboard"
        println("FRAGMENT_SELECTED In User Profile onCreate() After: $fragSelected")
        fragSelected = if (fragSelected == "Dashboard") {
            getFragment(UserFrag())
            btm_nav.setSelectedItemId(R.id.navMatch)
            "Dashboard"
        } else if (fragSelected == "Matches") {
            getFragment(MessagesFrag())
            btm_nav.setSelectedItemId(R.id.navMessages)
            "Matches"
        } else {
            getFragment(QRFrag())
            btm_nav.setSelectedItemId(R.id.navQR)
            "QR"
        }
        btm_nav.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navMatch -> {
                    setAnimLeft(UserFrag())
                    fragSelected = "Dashboard"
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navMessages -> {
                    if (fragSelected === "QR") {
                        setAnimLeft(MessagesFrag())
                    } else {
                        setAnimRight(MessagesFrag())
                    }
                    fragSelected = "Matches"
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navQR -> {
                    setAnimRight(QRFrag())
                    fragSelected = "QR"
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
        profileImageButton.setOnClickListener(View.OnClickListener { view: View? ->
            val intent = Intent(this, UserProfile::class.java)
            intent.putExtra("FRAGMENT_SELECTED", fragSelected)
            `var` = "Clicked"
            startActivity(intent)
        })
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData")
        storageRef = storage.reference
        emailAndSetImage
    }

    private val emailAndSetImage: Unit
        private get() {
            val user = mAuth!!.currentUser
            val uId = user!!.uid
            mDatabase!!.child(uId).get().addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("firebase", "Error getting data", task.exception)
                } else {
                    val user = task.result.value as HashMap<String, Any>?
                    val email = user!!["email"].toString()
                    val firstLogin = java.lang.Boolean.parseBoolean(user["firstLogin"].toString())
                    if (firstLogin) {
                        Picasso.get().load(R.drawable.defaultprofile).centerCrop().resize(60, 60)
                            .transform(
                                CircleTransform()
                            ).into(profileImageButton)
                    } else {
                        setImage(email)
                    }
                }
            }
        }

    fun setImage(email: String?) {
        val storageReference = FirebaseStorage.getInstance().reference
        val photoReference = storageReference.child("User").child(email!!).child("profilePic.png")
        photoReference.downloadUrl.addOnSuccessListener { uri -> // Got the download URL for 'users/me/profile.png'
            // Pass it to Picasso to download, show in ImageView and caching
            Picasso.get().load(uri.toString()).centerCrop().resize(60, 60)
                .transform(CircleTransform()).into(profileImageButton)
        }.addOnFailureListener {
            // Handle any errors
        }
    }

    fun getCroppedBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(
            (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(), (
                    bitmap.width / 2).toFloat(), paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    private fun setAnimRight(fragment: Fragment) {
        val fragmentTransaction = fm.beginTransaction().setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
    }

    private fun setAnimLeft(fragment: Fragment) {
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.enter_from_left,
            R.anim.exit_to_right,
            R.anim.enter_from_right,
            R.anim.exit_to_left
        )
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
    }

    private fun getFragment(fragment: Fragment) {
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
    }

    inner class CircleTransform : Transformation {
        override fun transform(source: Bitmap): Bitmap {
            val size = Math.min(source.width, source.height)
            val x = (source.width - size) / 2
            val y = (source.height - size) / 2
            val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
            if (squaredBitmap != source) {
                source.recycle()
            }
            val bitmap = Bitmap.createBitmap(size, size, source.config)
            val canvas = Canvas(bitmap)
            val paint = Paint()
            val shader = BitmapShader(
                squaredBitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP
            )
            paint.shader = shader
            paint.isAntiAlias = true
            val r = size / 2f
            canvas.drawCircle(r, r, r, paint)
            squaredBitmap.recycle()
            return bitmap
        }

        override fun key(): String {
            return "circle"
        }
    }

    public override fun onStart() {
        super.onStart()
        println("FRAGMENT_SELECTED In User Profile onStart() Before: $fragSelected")
        fragSelected =
            if (isNotNullOrEmpty(intent.getStringExtra("FRAGMENT_SELECTED"))) intent.getStringExtra(
                "FRAGMENT_SELECTED"
            ) else "Dashboard"
        println("FRAGMENT_SELECTED In User Profile onStart() After: $fragSelected")
        if (fragSelected == "Dashboard") {
            getFragment(UserFrag())
            btm_nav!!.selectedItemId = R.id.navMatch
            fragSelected = "Dashboard"
        } else if (fragSelected == "Matches") {
            getFragment(MessagesFrag())
            btm_nav!!.selectedItemId = R.id.navMessages
            fragSelected = "Matches"
        } else {
            getFragment(QRFrag())
            btm_nav!!.selectedItemId = R.id.navQR
            fragSelected = "QR"
        }
    }

    override fun onResume() {
        super.onResume()
        println("var$`var`")
        if (isNotNullOrEmpty(`var`)) {
            println("var")
            overridePendingTransition(R.transition.slide_in_up, R.transition.slide_out_up)
        } else {
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }
    }

    override fun onPause() {
        super.onPause()
        if (`var` === "Clicked") {
            overridePendingTransition(R.transition.slide_in_down, R.transition.slide_out_down)
        } else {
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }
        `var` = null
    }

    companion object {
        private fun isNotNullOrEmpty(str: String?): Boolean {
            return str != null && !str.isEmpty()
        }
    }
}