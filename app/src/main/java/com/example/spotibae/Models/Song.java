package com.example.spotibae.Models;

import java.util.List;

public class Song {
    public String id;
    public String name;
    public String artistName;
    public String href;
    public String uri;

    public Song() { }

    public Song(String id, String name, String artistName, String href, String uri) {
        this.id = id;
        this.artistName = artistName;
        this.name = name;
        this.href = href;
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Song: " + "ID: " + id + ", Artist Name: " + artistName + ", Album Name: " + name + ", Href: " + href + ", URI: " + uri + "\n";
    }
}