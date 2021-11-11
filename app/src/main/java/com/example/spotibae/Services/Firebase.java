package com.example.spotibae.Services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.spotibae.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Firebase {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private User newUser;

    public Firebase(FirebaseAuth mAuth, DatabaseReference mDatabase) {
        this.mAuth = mAuth;
        this.mDatabase = mDatabase;
    }

    public User getUserFromFirebase(String uId) {
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData");
        mAuth = FirebaseAuth.getInstance();
        mDatabase.child(uId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    HashMap<String, Object> user = (HashMap<String, Object>) task.getResult().getValue();
                    String firstName = user.get("firstName").toString();
                    String lastName = user.get("lastName").toString();
                    long age = (long) user.get("age");
                    String email = user.get("email").toString();
                    String fullName = user.get("fullName").toString();
                    String bio = user.get("bio").toString();
                    String gender = user.get("gender").toString();
                    String phoneNumber = user.get("phoneNumber").toString();
                    long distance = (long) user.get("distance");
                    long lowestAgePref = (long) user.get("lowestAgePref");
                    long highestAgePref = (long) user.get("highestAgePref");
                    String genderPref = user.get("genderPref").toString();
                    boolean spotifyVerified = (boolean) user.get("spotifyVerified");
                    String location = user.get("location").toString();
                    newUser = new User(email, fullName, firstName, lastName, age, bio, gender, phoneNumber, distance, lowestAgePref, highestAgePref, genderPref, spotifyVerified, location);
                }
            }
        });
        return newUser;
    }
}
