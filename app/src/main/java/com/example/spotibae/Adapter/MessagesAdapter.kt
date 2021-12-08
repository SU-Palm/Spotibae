package com.example.spotibae.Adapter

import android.content.Context
import com.example.spotibae.Models.ModelClass
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.example.spotibae.R
import com.example.spotibae.Adapter.Adapter.ViewHolder.MyClickListener
import android.content.Intent
import com.example.spotibae.Activities.Messaging.MessagingScreen
import com.example.spotibae.Activities.Messaging.MusicPlayer
import android.widget.TextView
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.FirebaseStorage
import com.google.android.gms.tasks.OnSuccessListener
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.android.gms.tasks.OnFailureListener
import android.graphics.PorterDuffXfermode
import android.graphics.PorterDuff
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.BaseAdapter
import com.example.spotibae.Models.Messages
import com.google.firebase.auth.FirebaseAuth
import java.util.ArrayList

//import com.example.spotibae.Adapter.MessagesAdapter.SenderViewHolder
//import com.example.spotibae.Adapter.MessagesAdapter.RecieverViewHolder

class MessagesAdapter() {

}
/*
class MessagesAdapter(val context: Context, var messagesArrayList: ArrayList<Messages>) :
    BaseAdapter() {
    var ITEM_SEND = 1
    var ITEM_RECIEVE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_SEND) {
            val view =
                LayoutInflater.from(context).inflate(R.layout.senderchatlayout, parent, false)
            SenderViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(context).inflate(R.layout.recieverchatlayout, parent, false)
            RecieverViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messages = messagesArrayList[position]
        if (holder.javaClass == SenderViewHolder::class.java) {
            val viewHolder = holder as SenderViewHolder
            viewHolder.textViewmessaage.text = messages.message
            viewHolder.timeofmessage.text = messages.currenttime
        } else {
            val viewHolder = holder as RecieverViewHolder
            viewHolder.textViewmessaage.text = messages.message
            viewHolder.timeofmessage.text = messages.currenttime
        }
    }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItem(p0: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(p0: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        TODO("Not yet implemented")
    }

    override fun getItemViewType(position: Int): Int {
        val messages = messagesArrayList[position]
        return if (FirebaseAuth.getInstance().currentUser!!.uid == messages.senderId) {
            ITEM_SEND
        } else {
            ITEM_RECIEVE
        }
    }

    override fun getItemCount(): Int {
        return messagesArrayList.size
    }

    internal inner class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewmessaage: TextView
        var timeofmessage: TextView

        init {
            textViewmessaage = itemView.findViewById(R.id.sendermessage)
            timeofmessage = itemView.findViewById(R.id.timeofmessage)
        }
    }

    internal inner class RecieverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewmessaage: TextView
        var timeofmessage: TextView

        init {
            textViewmessaage = itemView.findViewById(R.id.sendermessage)
            timeofmessage = itemView.findViewById(R.id.timeofmessage)
        }
    }

    override fun onBindViewHolder(holder: Any, position: Int) {
        return if (viewType == ITEM_SEND) {
            val view =
                LayoutInflater.from(context).inflate(R.layout.senderchatlayout, parent, false)
            SenderViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(context).inflate(R.layout.recieverchatlayout, parent, false)
            RecieverViewHolder(view)
        }
    }

}
*/