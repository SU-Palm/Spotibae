package com.example.spotibae.Services.Serializers

import com.example.spotibae.Models.Artist
import kotlin.Throws
import com.example.spotibae.Models.Song
import com.google.gson.*
import java.lang.reflect.Type

class ArtistDeserializer : JsonDeserializer<Artist> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement, typeOfT: Type,
        context: JsonDeserializationContext
    ): Artist {
        val jsonObject = json.asJsonObject
        val genresArray = jsonObject["genres"] as JsonArray
        val imageArray = jsonObject["images"] as JsonArray
        val id = jsonObject["id"].toString()
        val name = jsonObject["name"].toString()
        val href = jsonObject["href"].toString()
        val genre = genresArray[0].toString()
        val imageURI = imageArray[0].asJsonObject["url"].toString()
        val uri = jsonObject["uri"].toString()
        return Artist(id, name, href, genre, imageURI, uri)
    }
}