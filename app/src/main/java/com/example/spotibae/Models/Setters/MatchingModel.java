package com.example.spotibae.Models.Setters;

public class MatchingModel {
    String nameText;
    String ageText;
    String bioText;
    int imageview;
    String firebaseID;

    public MatchingModel(String firebaseID, int imageview, String nameText, String ageText, String bioText) {
        this.firebaseID = firebaseID;
        this.imageview = imageview;
        this.nameText = nameText;
        this.ageText = ageText;
        this.bioText = bioText;
    }

    public String getFirebaseId() { return firebaseID; }

    public int getImageview() {
        return imageview;
    }

    public String getNameText() {
        return nameText;
    }

    public String getAgeText() {
        return ageText;
    }

    public String getBioText() {
        return bioText;
    }

}
