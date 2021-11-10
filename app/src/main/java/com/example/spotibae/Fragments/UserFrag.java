package com.example.spotibae.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotibae.Models.ModelClass;
import com.example.spotibae.Models.Setters.MatchingModel;
import com.example.spotibae.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFrag extends Fragment {
    View view;
    List<MatchingModel> userList1 = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    HashMap<String, HashMap<String, Object>> users;
    TextView nameText;
    TextView ageText;
    TextView bioText;
    ImageView imageView;
    ImageView matchButton;
    ImageView denyButton;
    ImageView refreshButton;
    int count = 0;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFrag newInstance(String param1, String param2) {
        UserFrag fragment = new UserFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user, container, false);
        nameText = view.findViewById(R.id.nameText);
        ageText = view.findViewById(R.id.ageText);
        bioText = view.findViewById(R.id.bioText);
        imageView = view.findViewById(R.id.spotifyPictures);
        matchButton = view.findViewById(R.id.matchButton);
        denyButton = view.findViewById(R.id.denyButton);
        refreshButton = view.findViewById(R.id.refreshButton);
        setListeners();
        getUsers();
        return view;
    }

    public void getUsers() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uId = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData");
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("Firebase Users", String.valueOf(task.getResult().getValue()));
                    users = (HashMap<String, HashMap<String, Object>>) task.getResult().getValue();
                    users.remove(uId);
                    Log.d("Firebase Users", users.toString());
                    for(Map.Entry<String, HashMap<String, Object>> userList: users.entrySet()) {
                        HashMap<String, Object> userHash = userList.getValue();
                        userList1.add(new MatchingModel(userList.getKey(), R.drawable.acidrao, userHash.get("firstName").toString() + ", ", userHash.get("age").toString(), userHash.get("bio").toString()));
                    }
                    initView();
                }
            }
        });
        //Log.d("Firebase Users", users.toString());
    }

    public void initView() {
        nameText.setText(userList1.get(count).getNameText());
        ageText.setText(userList1.get(count).getAgeText());
        bioText.setText(userList1.get(count).getBioText());
        getEmailAndSetImage();
    }

    private void setListeners() {
        matchButton.setOnClickListener( view1 -> {
            int size = userList1.size();
            if(count < size - 1 ) {
                count += 1;
                initView();
            }
        });

        denyButton.setOnClickListener( view1 -> {
            int size = userList1.size();
            if(count < size - 1 ) {
                count += 1;
                initView();
            }
        });
    }

    private void getEmailAndSetImage() {
        String uId = userList1.get(count).getFirebaseId();
        mDatabase.child(uId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    HashMap<String, Object> user = (HashMap<String, Object>) task.getResult().getValue();
                    String email = user.get("email").toString();
                    setImage(email);
                }
            }
        });
    }

    public void setImage(String email) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child("User").child(email).child("favoriteAlbum.jpeg");

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap,  400 ,400, true));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

}