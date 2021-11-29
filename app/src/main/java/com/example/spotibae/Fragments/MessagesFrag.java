package com.example.spotibae.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spotibae.Adapter.Adapter;
import com.example.spotibae.Models.ModelClass;
import com.example.spotibae.R;
import com.google.android.gms.tasks.OnCompleteListener;
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
 * Use the {@link MessagesFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagesFrag extends Fragment {

    RecyclerView mrecyclerView;
    LinearLayoutManager layoutManager;
    List<ModelClass> userList;
    Adapter adapter;
    View view;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("UserData");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    StorageReference profileRef;
    List<String> matchList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MessagesFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessagesFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static MessagesFrag newInstance(String param1, String param2) {
        MessagesFrag fragment = new MessagesFrag();
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

    private void initRecyclerView() {
        mrecyclerView = view.findViewById(R.id.recyclerView);
        mrecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mrecyclerView.setAdapter(new Adapter(userList));
    }

    private void initData() throws InterruptedException {
        userList = new ArrayList<>();
        getMatches();
    }

    public void getMatches() {
        FirebaseUser user = mAuth.getCurrentUser();
        String uId = user.getUid();

        mDatabase.child(uId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase User", String.valueOf(task.getResult().getValue()));
                    HashMap<String, Object> user = (HashMap<String, Object>) task.getResult().getValue();
                    HashMap<String, Object> matchesUsers = (HashMap<String, Object>) user.get("matches");
                    for(Map.Entry<String, Object> userList1: matchesUsers.entrySet()) {
                        String firebaseUid = userList1.getKey();
                        matchList.add(firebaseUid);
                    }
                    for(String firebaseID :matchList) {
                        getMatchesInfo(firebaseID);
                    }

                }
            }
        });
    }

    public void getMatchesInfo(String id) {
        mDatabase.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase User", String.valueOf(task.getResult().getValue()));
                    HashMap<String, Object> user = (HashMap<String, Object>) task.getResult().getValue();
                    userList.add(new ModelClass(id, user.get("email").toString(), user.get("firstName").toString(), "How ?","11:55 pm","_______________________________________"));
                }
                initRecyclerView();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_messages, container, false);
        try {
            initData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return view;
    }
}