package com.example.spotibae.Models;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String firstName;
    public String lastName;
    public long age;
    public String email;
    public String fullName;
    public String bio;
    public String gender;
    public String phoneNumber;
    public long distance;
    public long lowestAgePref;
    public long highestAgePref;
    public String genderPref;
    public boolean spotifyVerified;
    public String location;
    public List<Artist> favoriteArtists;
    public List<Song> favoriteSongs;
    public List<Album> favoriteAlbums;
    public List<User> matches;

    public User() { }

    public User(String email, String name) {
        this.email = email;
        this.fullName = name;
        this.firstName = name.substring(0, name.indexOf(" "));
        this.lastName = name.substring(name.indexOf(" ") + 1);
        this.age = 18;
        this.bio = "";
        this.gender = "Not Set";
        this.phoneNumber = "Not Set";
        this.distance = 0;
        this.lowestAgePref = 18;
        this.highestAgePref = 100;
        this.genderPref = "Not Set";
        this.spotifyVerified = false;
        this.location = "Not Set";
        this.favoriteArtists = new ArrayList<Artist>();
        this.favoriteSongs = new ArrayList<Song>();
        this.favoriteAlbums = new ArrayList<Album>();
        this.matches = new ArrayList<User>();
    }

    public User(String email, String fullName, String firstName, String lastName, long age, String bio, String gender, String phoneNumber, long distance, long lowestAgePref, long highestAgePref, String genderPref, boolean spotifyVerified, String location) {
        this.email = email;
        this.fullName = fullName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.bio = bio;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.distance = distance;
        this.lowestAgePref = lowestAgePref;
        this.highestAgePref = highestAgePref;
        this.genderPref = genderPref;
        this.spotifyVerified = spotifyVerified;
        this.location = location;
        this.favoriteArtists = new ArrayList<Artist>();
        this.favoriteSongs = new ArrayList<Song>();
        this.favoriteAlbums = new ArrayList<Album>();
        this.matches = new ArrayList<User>();
    }

    @Override
    public String toString() {
        return "\n" + "User: " + this.fullName + "\n"
                + "Age: " + this.age + "\n"
                + "Email: " + this.email + "\n"
                + "Bio: " + this.bio;
    }
}
