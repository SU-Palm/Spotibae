package com.example.spotibae.Models

class Song(
    var id: String,
    var name: String,
    var artistName: String,
    var href: String,
    var uri: String
) {

    override fun toString(): String {
        return "Song: ID: $id, Artist Name: $artistName, Album Name: $name, Href: $href, URI: $uri\n"
    }
}