package com.example.spotibae.Models

class Album {
    var id = 0
    var artistName: String? = null
    var name: String? = null
    var href: String? = null
    var genre: String? = null
    var imageURI: String? = null
    var uri: String? = null

    constructor(
        id: Int,
        artistName: String?,
        name: String?,
        href: String?,
        genre: String?,
        imageURI: String?,
        uri: String?
    ) {
        this.id = id
        this.artistName = artistName
        this.name = name
        this.href = href
        this.genre = genre
        this.imageURI = imageURI
        this.uri = uri
    }

    override fun toString(): String {
        return "Album: ID: $id, Artist Name: $artistName, Album Name: $name, Href: $href, Genre: $genre, Image URI: $imageURI, URI: $uri\n"
    }
}