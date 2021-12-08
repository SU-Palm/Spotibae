package com.example.spotibae.Models

class Album(var id: Int, var artistName: String, var name: String, var href: String, var genre: String, var imageURI: String, var uri: String) {

    override fun toString(): String {
        return "Album: ID: $id, Artist Name: $artistName, Album Name: $name, Href: $href, Genre: $genre, Image URI: $imageURI, URI: $uri\n"
    }
}