package com.example.spotibae.Adapter

import com.example.spotibae.Models.ModelClass
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.spotibae.R
import com.example.spotibae.Adapter.Adapter.ViewHolder.MyClickListener
import android.content.Intent
import android.graphics.*
import com.example.spotibae.Activities.Messaging.MessagingScreen
import com.example.spotibae.Activities.Messaging.MusicPlayer
import android.widget.TextView
import com.google.firebase.storage.FirebaseStorage
import android.view.View
import android.widget.ImageView

class Adapter(private val userList: List<ModelClass>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_chat_user, parent, false)
        return ViewHolder(view, object : MyClickListener {
            override fun onMessages(p: Int) {
                val intent = Intent(view.context, MessagingScreen::class.java)
                intent.putExtra("PROFILE_PIC_EMAIL", userList[p].email)
                intent.putExtra("receiveruid", userList[p].firebaseId)
                intent.putExtra("name", userList[p].textview1)
                view.context.startActivity(intent)
            }

            override fun onMusic(p: Int) {
                val intent = Intent(view.context, MusicPlayer::class.java)
                intent.putExtra("PROFILE_PIC_EMAIL", userList[p].email)
                intent.putExtra("USER_FIREBASE_ID", userList[p].firebaseId)
                intent.putExtra("USER_NAME", userList[p].textview1)
                view.context.startActivity(intent)
            }
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val firebaseID = userList[position].firebaseId
        val email = userList[position].email
        val name = userList[position].textview1
        val msg = userList[position].textview2
        val time = userList[position].textview3
        holder.setData(firebaseID, email, name, msg, time)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    //view holder class
    class ViewHolder(itemView: View, listener: MyClickListener) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var listener: MyClickListener
        var imageView: ImageView
        private val textView: TextView
        private val textView2: TextView
        private val textview3: TextView
        var music: ImageView
        var messages: ImageView
        fun setData(
            firebaseId: String?,
            email: String?,
            name: String?,
            msg: String?,
            time: String?
        ) {
            //imageView.setImageResource(resource);
            setImage(email)
            textView.text = name
            textView2.text = msg
            textview3.text = time
        }

        override fun onClick(view: View) {
            when (view.id) {
                R.id.messagesButton -> listener.onMessages(this.layoutPosition)
                R.id.musicPlayerButton -> listener.onMusic(this.layoutPosition)
                else -> {
                }
            }
        }

        interface MyClickListener {
            fun onMessages(p: Int)
            fun onMusic(p: Int)
        }

        fun setImage(email: String?) {
            val storageReference = FirebaseStorage.getInstance().reference
            val photoReference =
                storageReference.child("User").child(email!!).child("profilePic.png")
            val ONE_MEGABYTE = (1024 * 1024).toLong()
            photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 90, 90, true))
            }.addOnFailureListener { }
        }

        init {
            //here use xml ids
            //give different name not like constructor
            imageView = itemView.findViewById(R.id.imageview)
            textView = itemView.findViewById(R.id.textview)
            textView2 = itemView.findViewById(R.id.textview2)
            textview3 = itemView.findViewById(R.id.textview3)
            music = itemView.findViewById(R.id.musicPlayerButton)
            messages = itemView.findViewById(R.id.messagesButton)
            this.listener = listener
            music.setOnClickListener(this)
            messages.setOnClickListener(this)
        }
    }
}