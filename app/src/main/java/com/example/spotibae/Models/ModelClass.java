package com.example.spotibae.Models;

public class ModelClass {

    private String firebaseID;
    private String email;
    private String textview1;
    private String textview2;
    private String textview3;

    //new code
    private String divider;

    public ModelClass(String firebaseID, String email, String textview1, String textview2, String textview3, String divider) {
        this.firebaseID=firebaseID;
        this.email = email;
        this.textview1=textview1;
        this.textview2=textview2;
        this.textview3=textview3;
        this.divider=divider;
    }

    public String getFirebaseId() {
        return firebaseID;
    }

    public String getEmail() {
        return email;
    }

    public String getTextview1() {
        return textview1;
    }

    public String getDivider()
    {
        return divider;
    }

    public String getTextview2() {
        return textview2;
    }

    public String getTextview3() {
        return textview3;
    }
}
