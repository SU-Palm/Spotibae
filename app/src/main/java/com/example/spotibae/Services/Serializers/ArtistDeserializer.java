package com.example.spotibae.Services.Serializers;

import com.example.spotibae.Models.Artist;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class ArtistDeserializer implements JsonDeserializer<Artist> {
    @Override
    public Artist deserialize(JsonElement json, Type typeOfT,
                              JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        JsonArray genresArray = (JsonArray) jsonObject.get("genres");
        JsonArray imageArray = (JsonArray) jsonObject.get("images");

        String id = jsonObject.get("id").toString();
        String name = jsonObject.get("name").toString();
        String href = jsonObject.get("href").toString();
        String genre = genresArray.get(0).toString();
        String imageURI = imageArray.get(0).getAsJsonObject().get("url").toString();
        String uri = jsonObject.get("uri").toString();

        return new Artist(id, name, href, genre, imageURI, uri);
    }
}