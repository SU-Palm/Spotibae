package com.example.spotibae.Models.Setters;

import java.util.List;

public class MatchingModel {
    String nameText;
    String ageText;
    String bioText;
    List<String> uriList;
    String firebaseID;

    public MatchingModel(String firebaseID, List<String> uriList, String nameText, String ageText, String bioText) {
        this.firebaseID = firebaseID;
        this.uriList = uriList;
        this.nameText = nameText;
        this.ageText = ageText;
        this.bioText = bioText;
    }

    public String getFirebaseId() { return firebaseID; }

    public List<String> getImageview() {
        return uriList;
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
