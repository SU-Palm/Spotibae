package com.example.spotibae.Models

import java.util.HashMap

class User {
    var firstName: String? = null
    var lastName: String? = null
    var age: Long = 0
    var email: String? = null
    var fullName: String? = null
    var bio: String? = null
    var gender: String? = null
    var phoneNumber: String? = null
    var distance: Long = 0
    var lowestAgePref: Long = 0
    var highestAgePref: Long = 0
    var genderPref: String? = null
    var spotifyVerified = false
    var location: String? = null
    var favoriteSongs: String? = null
    var favoriteArtists: String? = null
    var hashHashArtists: MutableMap<String, Map<String, String>>? = null
    var hashHashSongs: MutableMap<String, Map<String, String>>? = null

    constructor() {}
    constructor(email: String?, name: String) {
        this.email = email
        fullName = name
        firstName = name.substring(0, name.indexOf(" "))
        lastName = name.substring(name.indexOf(" ") + 1)
        age = 18
        bio = ""
        gender = "Not Set"
        phoneNumber = "Not Set"
        distance = 0
        lowestAgePref = 18
        highestAgePref = 100
        genderPref = "Not Set"
        spotifyVerified = false
        location = "Not Set"
        favoriteSongs = ""
        favoriteArtists = ""
        hashHashArtists = HashMap()
        (hashHashArtists as HashMap<String, Map<String, String>>)["0"] = HashMap()
        (hashHashArtists as HashMap<String, Map<String, String>>)["1"] = HashMap()
        hashHashSongs = HashMap()
        (hashHashSongs as HashMap<String, Map<String, String>>)["0"] = HashMap()
        (hashHashSongs as HashMap<String, Map<String, String>>)["1"] = HashMap()
    }

    constructor(
        email: String?,
        fullName: String?,
        firstName: String?,
        lastName: String?,
        age: Long,
        bio: String?,
        gender: String?,
        phoneNumber: String?,
        distance: Long,
        lowestAgePref: Long,
        highestAgePref: Long,
        genderPref: String?,
        spotifyVerified: Boolean,
        location: String?
    ) {
        this.email = email
        this.fullName = fullName
        this.firstName = firstName
        this.lastName = lastName
        this.age = age
        this.bio = bio
        this.gender = gender
        this.phoneNumber = phoneNumber
        this.distance = distance
        this.lowestAgePref = lowestAgePref
        this.highestAgePref = highestAgePref
        this.genderPref = genderPref
        this.spotifyVerified = spotifyVerified
        this.location = location
        favoriteSongs = ""
        favoriteArtists = ""
        hashHashArtists = HashMap()
        (hashHashArtists as HashMap<String, Map<String, String>>)["0"] = HashMap()
        (hashHashArtists as HashMap<String, Map<String, String>>)["1"] = HashMap()
        hashHashSongs = HashMap()
        (hashHashSongs as HashMap<String, Map<String, String>>)["0"] = HashMap()
        (hashHashSongs as HashMap<String, Map<String, String>>)["1"] = HashMap()
    }

    override fun toString(): String {
        return """
            
            User: ${fullName}
            Age: ${age}
            Email: ${email}
            Bio: ${bio}
            """.trimIndent()
    }
}