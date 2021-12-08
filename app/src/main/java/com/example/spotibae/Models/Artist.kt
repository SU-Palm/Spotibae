package com.example.spotibae.Models

class Artist {
    var id: String? = null
    var name: String? = null
    var href: String? = null
    var genre: String? = null
    var imageURI: String? = null
    var uri: String? = null

    constructor() {}
    constructor(
        id: String?,
        name: String?,
        href: String?,
        genre: String?,
        imageURI: String?,
        uri: String?
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