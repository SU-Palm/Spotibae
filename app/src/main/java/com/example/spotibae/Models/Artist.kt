package com.example.spotibae.Models

class Artist {
    lateinit var id: String
    lateinit var name: String
    lateinit var href: String
    lateinit var genre: String
    lateinit var imageURI: String
    lateinit var uri: String

    constructor() {}
    constructor(
        id: String,
        name: String,
        href: String,
        genre: String,
        imageURI: String,
        uri: String
    ) {
        this.id = id
        this.name = name
        this.href = href
        this.genre = genre
        this.imageURI = imageURI
        this.uri = uri
    }

    override fun toString(): String {
        return "Artist: ID: $id, Name: $name, Href: $href, Genre: $genre, Image URI: $imageURI, URI: $uri\n"
    }
}