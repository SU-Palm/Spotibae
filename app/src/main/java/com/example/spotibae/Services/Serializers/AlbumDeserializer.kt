package com.example.spotibae.Services.Serializers

import com.example.spotibae.Models.Album
import kotlin.Throws
import com.example.spotibae.Models.Song
import com.google.gson.*
import java.lang.reflect.Type

class AlbumDeserializer : JsonDeserializer<Album> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement, typeOfT: Type,
        context: JsonDeserializationContext
    ): Album {
        val jsonObject = json.asJsonObject
        val artistArray = jsonObject["artists"] as JsonArray
        val genresArray = jsonObject["genres"] as JsonArray
        val imageArray = jsonObject["images"] as JsonArray
        val id = jsonObject["id"].asInt
        val artistName = artistArray[0].asJsonObject["name"].toString()
        val name = jsonObject["name"].toString()
        val href = jsonObject["href"].toString()
        val genre = genresArray[0].toString()
        val imageURI = imageArray[0].asJsonObject["url"].toString()
        val uri = jsonObject["uri"].toString()
        return Album(id, artistName, name, href, genre, imageURI, uri)
    }
}