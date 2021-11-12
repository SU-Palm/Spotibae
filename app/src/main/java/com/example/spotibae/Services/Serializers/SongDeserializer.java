package com.example.spotibae.Services.Serializers;

import com.example.spotibae.Models.Song;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class SongDeserializer implements JsonDeserializer<Song> {
    @Override
    public Song deserialize(JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject jsonObject = json.getAsJsonObject();

        JsonArray artistArray = (JsonArray) jsonObject.get("artists");

        String id = jsonObject.get("id").toString();
        String artistName = artistArray.get(0).getAsJsonObject().get("name").toString();
        String name = jsonObject.get("name").toString();
        String href = jsonObject.get("href").toString();
        String uri = jsonObject.get("uri").toString();

        return new Song(id, name, artistName, href, uri);
    }
}
