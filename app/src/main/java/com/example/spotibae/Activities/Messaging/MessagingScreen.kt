package com.example.spotibae.Activities.Messaging

import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.content.Intent
import android.graphics.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import com.example.spotibae.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.example.spotibae.Activities.Welcome.BaseActivity
import com.google.firebase.storage.FirebaseStorage
import android.view.View
import android.widget.*
import com.example.spotibae.Adapter.MessagesAdapter
import com.example.spotibae.Models.Messages
import java.text.SimpleDateFormat
import java.util.*

class MessagingScreen : AppCompatActivity() {
    var mgetmessage: EditText? = null
    var msendmessagebutton: ImageButton? = null
    var msendmessagecardview: CardView? = null
    var mimageviewofspecificuser: ImageView? = null
    var mnameofspecificuser: TextView? = null
    private var enteredmessage: String? = null
    var intent = null
    var mrecievername: String? = null
    var sendername: String? = null
    var mrecieveruid: String? = null
    var msenderuid: String? = null
    private var firebaseAuth: FirebaseAuth? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var senderroom: String? = null
    var recieverroom: String? = null
    var mbackbuttonofspecificchat: ImageButton? = null
    var mmessagerecyclerview: RecyclerView? = null
    var currenttime: String? = null
    var calendar: Calendar? = null
    var simpleDateFormat: SimpleDateFormat? = null
    var messagesAdapter: MessagesAdapter? = null
    var messagesArrayList: ArrayList<Messages?>? = null
    var userEmail: String? = null
    var backButton: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging_screen)
        mgetmessage = findViewById(R.id.getmessage)
        msendmessagecardview = findViewById(R.id.carviewofsendmessage)
        msendmessagebutton = findViewById(R.id.imageviewsendmessage)
        mnameofspecificuser = findViewById(R.id.userName)
        mimageviewofspecificuser = findViewById(R.id.profilePic)
        messagesArrayList = ArrayList()
        mmessagerecyclerview = findViewById(R.id.recyclerviewofspecific)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        mmessagerecyclerview?.setLayoutManager(linearLayoutManager)
        messagesAdapter = MessagesAdapter(this@MessagingScreen, messagesArrayList)
        mmessagerecyclerview?.setAdapter(messagesAdapter)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        calendar = Calendar.getInstance()
        simpleDateFormat = SimpleDateFormat("hh:mm a")
        msenderuid = firebaseAuth!!.uid
        mrecieveruid = getIntent().getStringExtra("receiveruid")
        mrecievername = getIntent().getStringExtra("name")
        userEmail = getIntent().getStringExtra("PROFILE_PIC_EMAIL")
        senderroom = msenderuid + mrecieveruid
        recieverroom = mrecieveruid + msenderuid
        setImage(userEmail)
        mnameofspecificuser?.setText(mrecievername)
        val databaseReference = firebaseDatabase!!.reference.child("chats").child(
            senderroom!!
        ).child("messages")
        messagesAdapter = MessagesAdapter(this@MessagingScreen, messagesArrayList)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messagesArrayList!!.clear()
                for (snapshot1 in snapshot.children) {
                    val messages = snapshot1.getValue(
                        Messages::class.java
                    )
                    println("Printing Messages: " + messages.toString())
                    messagesArrayList!!.add(messages)
                }
                messagesAdapter = MessagesAdapter(this@MessagingScreen, messagesArrayList)
                mmessagerecyclerview?.setAdapter(messagesAdapter)
                messagesAdapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        msendmessagebutton?.setOnClickListener {
            enteredmessage = mgetmessage?.getText().toString()
            if (enteredmessage!!.isEmpty()) {
                Toast.makeText(applicationContext, "Enter message first", Toast.LENGTH_SHORT).show()
            } else {
                val date = Date()
                currenttime = simpleDateFormat!!.format(calendar?.time)
                val messages = Messages(enteredmessage, firebaseAuth!!.uid, date.time, currenttime)
                firebaseDatabase = FirebaseDatabase.getInstance()
                firebaseDatabase!!.reference.child("chats")
                    .child(senderroom!!)
                    .child("messages")
                    .push().setValue(messages).addOnCompleteListener {
                        firebaseDatabase!!.reference
                            .child("chats")
                            .child(recieverroom!!)
                            .child("messages")
                            .push()
                            .setValue(messages).addOnCompleteListener { }
                    }
                mgetmessage?.setText(null)
            }
        }
        backButton = findViewById(R.id.backButton)
        backButton?.setOnClickListener(View.OnClickListener { view: View? ->
            val intent = Intent(this, BaseActivity::class.java)
            intent.putExtra("FRAGMENT_SELECTED", "Matches")
            startActivity(intent)
        })
    }

    fun setImage(email: String?) {
        val storageReference = FirebaseStorage.getInstance().reference
        val photoReference = storageReference.child("User").child(email!!).child("profilePic.png")
        val ONE_MEGABYTE = (1024 * 1024).toLong()
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            mimageviewofspecificuser!!.setImageBitmap(
                Bitmap.createScaledBitmap(
                    getCroppedBitmap(
                        bitmap
                    ), 90, 90, true
                )
            )
        }.addOnFailureListener { }
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

    public override fun onStart() {
        super.onStart()
        messagesAdapter!!.notifyDataSetChanged()
    }

    public override fun onStop() {
        super.onStop()
        if (messagesAdapter != null) {
            messagesAdapter!!.notifyDataSetChanged()
        }
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