package com.example.spotibae.Activities.Messaging

import android.annotation.SuppressLint
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
import android.widget.*
import com.example.spotibae.Adapter.MessagesAdapter
import com.example.spotibae.Models.Messages
import java.text.SimpleDateFormat
import java.util.*

class MessagingScreen : AppCompatActivity() {
    private lateinit var mGetMessage: EditText
    private lateinit var mSendMessageButton: ImageButton
    private lateinit var mSendMessageCardView: CardView
    private lateinit var mImageViewOfSpecificUser: ImageView
    private lateinit var mNameOfSpecificUser: TextView
    private lateinit var enteredMessage: String
    private var intent = null
    private lateinit var mRecieverName: String
    private lateinit var sendername: String
    private lateinit var mRecieverUid: String
    private lateinit var mSenderUid: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var senderRoom: String
    private lateinit var recieverRoom: String
    private lateinit var mbackbuttonofspecificchat: ImageButton
    private lateinit var mMessageRecyclerView: RecyclerView
    private lateinit var currentTime: String
    private lateinit var calendar: Calendar
    private lateinit var simpleDateFormat: SimpleDateFormat
    private lateinit var messagesAdapter: MessagesAdapter
    private lateinit var messagesArrayList: ArrayList<Messages>
    private lateinit var userEmail: String
    private lateinit var backButton: ImageView

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging_screen)
        mGetMessage = findViewById(R.id.getmessage)
        mSendMessageCardView = findViewById(R.id.carviewofsendmessage)
        mSendMessageButton = findViewById(R.id.imageviewsendmessage)
        mNameOfSpecificUser = findViewById(R.id.userName)
        mImageViewOfSpecificUser = findViewById(R.id.profilePic)
        messagesArrayList = ArrayList()
        mMessageRecyclerView = findViewById(R.id.recyclerviewofspecific)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        mMessageRecyclerView.layoutManager = linearLayoutManager
        messagesAdapter = MessagesAdapter(this@MessagingScreen, messagesArrayList)
        mMessageRecyclerView.adapter = messagesAdapter
        mAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        calendar = Calendar.getInstance()
        simpleDateFormat = SimpleDateFormat("hh:mm a")
        mSenderUid = mAuth.uid.toString()
        mRecieverUid = getIntent().getStringExtra("receiveruid").toString()
        mRecieverName = getIntent().getStringExtra("name").toString()
        userEmail = getIntent().getStringExtra("PROFILE_PIC_EMAIL").toString()

        senderRoom = mSenderUid + mRecieverUid
        recieverRoom = mRecieverUid + mSenderUid
        setImage(userEmail)
        mNameOfSpecificUser.text = mRecieverName
        val databaseReference = firebaseDatabase.reference.child("chats").child(senderRoom).child("messages")
        messagesAdapter = MessagesAdapter(this@MessagingScreen, messagesArrayList)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messagesArrayList.clear()
                for (snapshot1 in snapshot.children) {
                    val messages = snapshot1.getValue(Messages::class.java)
                    if (messages != null) {
                        messagesArrayList.add(messages)
                    }
                }
                messagesAdapter = MessagesAdapter(this@MessagingScreen, messagesArrayList)
                mMessageRecyclerView.adapter = messagesAdapter
                messagesAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        mSendMessageButton.setOnClickListener {
            enteredMessage = mGetMessage.text.toString()
            if (enteredMessage.isEmpty()) {
                Toast.makeText(applicationContext, "Enter message first", Toast.LENGTH_SHORT).show()
            } else {
                val date = Date()
                currentTime = simpleDateFormat.format(calendar.time)
                val messages = Messages(enteredMessage, mAuth.uid.toString(), date.time, currentTime)
                firebaseDatabase = FirebaseDatabase.getInstance()
                firebaseDatabase.reference.child("chats").child(senderRoom).child("messages")
                    .push().setValue(messages).addOnCompleteListener {
                        firebaseDatabase.reference
                            .child("chats")
                            .child(recieverRoom)
                            .child("messages")
                            .push()
                            .setValue(messages).addOnCompleteListener { }
                    }
                mGetMessage.text = null
            }
        }
        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, BaseActivity::class.java)
            intent.putExtra("FRAGMENT_SELECTED", "Matches")
            startActivity(intent)
        }
    }

    private fun setImage(email: String) {
        val storageReference = FirebaseStorage.getInstance().reference
        val photoReference = storageReference.child("User").child(email).child("profilePic.png")
        val ONE_MEGABYTE = (1024 * 1024).toLong()
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            mImageViewOfSpecificUser.setImageBitmap(
                Bitmap.createScaledBitmap(
                    getCroppedBitmap(
                        bitmap
                    ), 90, 90, true
                )
            )
        }.addOnFailureListener { }
    }

    private fun getCroppedBitmap(bitmap: Bitmap): Bitmap {
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
        messagesAdapter.notifyDataSetChanged()
    }

    public override fun onStop() {
        super.onStop()
        if (messagesAdapter != null) {
            messagesAdapter.notifyDataSetChanged()
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