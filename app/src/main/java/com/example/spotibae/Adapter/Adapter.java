package com.example.spotibae.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spotibae.Activities.Messaging.MessagingScreen;
import com.example.spotibae.Activities.Messaging.MusicPlayer;
import com.example.spotibae.Activities.User.UserProfile;
import com.example.spotibae.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotibae.Models.ModelClass;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<ModelClass> userList;

    public Adapter(List<ModelClass>userList) {
        this.userList=userList;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_chat_user, parent,false);
        ViewHolder holder = new ViewHolder(view, new ViewHolder.MyClickListener() {
            @Override
            public void onMessages(int p) {
                Intent intent = new Intent(view.getContext(), MessagingScreen.class);
                view.getContext().startActivity(intent);
            }

            @Override
            public void onMusic(int p) {
                Intent intent = new Intent(view.getContext(), MusicPlayer.class);
                view.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        int resource = userList.get(position).getImageview();
        String name=userList.get(position).getTextview1();
        String msg=userList.get(position).getTextview2();
        String time=userList.get(position).getTextview3();
        holder.setData(resource,name,msg,time);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    //view holder class

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MyClickListener listener;
        private ImageView imageView;
        private TextView textView;
        private TextView textView2;
        private TextView textview3;
        ImageView music;
        ImageView messages;

        public ViewHolder(@NonNull View itemView, MyClickListener listener) {
            super(itemView);
            //here use xml ids
            //give different name not like constructor
            imageView = itemView.findViewById(R.id.imageview);
            textView = itemView.findViewById(R.id.textview);
            textView2 = itemView.findViewById(R.id.textview2);
            textview3 = itemView.findViewById(R.id.textview3);
            music = itemView.findViewById(R.id.musicPlayerButton);
            messages = itemView.findViewById(R.id.messagesButton);

            this.listener = listener;

            music.setOnClickListener(this);
            messages.setOnClickListener(this);
        }

        public void setData(int resource, String name, String msg, String time) {
            imageView.setImageResource(resource);
            textView.setText(name);
            textView2.setText(msg);
            textview3.setText(time);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.messagesButton:
                    listener.onMessages(this.getLayoutPosition());
                    break;
                case R.id.musicPlayerButton:
                    listener.onMusic(this.getLayoutPosition());
                    break;
                default:
                    break;
            }
        }

        public interface MyClickListener {
            void onMessages(int p);
            void onMusic(int p);
        }
    }
}