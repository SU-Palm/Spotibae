package com.example.spotibae.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotibae.Activities.Messaging.MessagingScreen;
import com.example.spotibae.Activities.Messaging.MusicPlayer;
import com.example.spotibae.Activities.User.UserProfile;
import com.example.spotibae.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotibae.Models.ModelClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
                intent.putExtra("PROFILE_PIC_EMAIL", userList.get(p).getEmail());
                intent.putExtra("USER_FIREBASE_ID", userList.get(p).getFirebaseId());
                intent.putExtra("USER_NAME", userList.get(p).getTextview1());
                view.getContext().startActivity(intent);
            }

            @Override
            public void onMusic(int p) {
                Intent intent = new Intent(view.getContext(), MusicPlayer.class);
                intent.putExtra("PROFILE_PIC_EMAIL", userList.get(p).getEmail());
                intent.putExtra("USER_FIREBASE_ID", userList.get(p).getFirebaseId());
                intent.putExtra("USER_NAME", userList.get(p).getTextview1());
                view.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        String firebaseID = userList.get(position).getFirebaseId();
        String email = userList.get(position).getEmail();
        String name=userList.get(position).getTextview1();
        String msg=userList.get(position).getTextview2();
        String time=userList.get(position).getTextview3();
        holder.setData(firebaseID,email,name,msg,time);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    //view holder class

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MyClickListener listener;
        public ImageView imageView;
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

        public void setData(String firebaseId, String email, String name, String msg, String time) {
            //imageView.setImageResource(resource);
            setImage(email);
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

        public void setImage(String email) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference photoReference = storageReference.child("User").child(email).child("profilePic.png");

            final long ONE_MEGABYTE = 1024 * 1024;
            photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap,  90,90, true));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        }

        public Bitmap getCroppedBitmap(Bitmap bitmap) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                    bitmap.getWidth() / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        }
    }
}