package com.example.spotibae.Models

class Artist(var id: String, var name: String, var href: String, var genre: String, var imageURI: String, var uri: String) {

    override fun toString(): String {
        return "Artist: ID: $id, Name: $name, Href: $href, Genre: $genre, Image URI: $imageURI, URI: $uri\n"
    }
}