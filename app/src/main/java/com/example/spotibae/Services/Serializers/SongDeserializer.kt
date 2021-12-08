package com.example.spotibae.Services.Serializers

import kotlin.Throws
import com.example.spotibae.Models.Song
import com.google.gson.*
import java.lang.reflect.Type

class SongDeserializer : JsonDeserializer<Song> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement, typeOfT: Type,
        context: JsonDeserializationContext
    ): Song {
        val jsonObject = json.asJsonObject
        val artistArray = jsonObject["artists"] as JsonArray
        val id = jsonObject["id"].toString()
        val artistName = artistArray[0].asJsonObject["name"].toString()
        val name = jsonObject["name"].toString()
        val href = jsonObject["href"].toString()
        val uri = jsonObject["uri"].toString()
        return Song(id, name, artistName, href, uri)
    }
}