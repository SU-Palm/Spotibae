package com.example.spotibae.Models

class Song {
    lateinit var id: String
    lateinit var name: String
    lateinit var artistName: String
    lateinit var href: String
    lateinit var uri: String

    constructor() {}
    constructor(id: String, name: String, artistName: String, href: String, uri: String) {
        this.id = id
        this.artistName = artistName
        this.name = name
        this.href = href
        this.uri = uri
    }

    override fun toString(): String {
        return "Song: ID: $id, Artist Name: $artistName, Album Name: $name, Href: $href, URI: $uri\n"
    }
}