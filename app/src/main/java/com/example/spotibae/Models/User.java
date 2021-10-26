package com.example.spotibae.Models;

import com.google.firebase.auth.FirebaseUser;

public class User {
    public String firstName;
    public String lastName;
    public int age;
    public String email;
    public String fullName;
    public String bio;

    public User() { }

    public User(String mail, String name) {
        this.email = mail;
        this.fullName = name;
        this.firstName = name.substring(0, name.indexOf(" "));
        this.lastName = name.substring(name.indexOf(" ") + 1);
        this.age = 0;
        this.bio = "";
    }

    @Override
    public String toString() {
        return "\n" + "User: " + this.fullName + "\n"
                + "Age: " + this.age + "\n"
                + "Email: " + this.email + "\n"
                + "Bio: " + this.bio;
    }
}
