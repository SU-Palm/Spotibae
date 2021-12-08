package com.example.spotibae.Adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.spotibae.R
import com.google.firebase.auth.FirebaseAuth
import android.widget.TextView
import com.example.spotibae.Models.Messages
import java.util.ArrayList

class MessagesAdapter(var context: Context, var messagesArrayList: ArrayList<Messages?>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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

    override fun getItemViewType(position: Int): Int {
        val messages = messagesArrayList!![position]
        return if (FirebaseAuth.getInstance().currentUser!!.uid == messages?.senderId) {
            ITEM_SEND
        } else {
            ITEM_RECIEVE
        }
    }

    override fun getItemCount(): Int {
        return messagesArrayList!!.size
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messages = messagesArrayList!![position]
        if (holder.javaClass == SenderViewHolder::class.java) {
            val viewHolder = holder as SenderViewHolder
            viewHolder.textViewmessaage.text = messages?.message
            viewHolder.timeofmessage.text = messages?.currenttime
        } else {
            val viewHolder = holder as RecieverViewHolder
            viewHolder.textViewmessaage.text = messages?.message
            viewHolder.timeofmessage.text = messages?.currenttime
        }
    }
}