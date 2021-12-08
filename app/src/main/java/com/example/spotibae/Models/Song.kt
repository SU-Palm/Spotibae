package com.example.spotibae.Models

class Song {
    var id: String? = null
    var name: String? = null
    var artistName: String? = null
    var href: String? = null
    var uri: String? = null

    constructor() {}
    constructor(id: String?, name: String?, artistName: String?, href: String?, uri: String?) {
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