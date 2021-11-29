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
/*
{
  "items": [
    {
      "artists": [
          {
            "external_urls": {
              "spotify": "https://open.spotify.com/artist/0TnOYISbd1XYRBk9myaseg"
            },
            "href": "https://api.spotify.com/v1/artists/0TnOYISbd1XYRBk9myaseg",
            "id": "0TnOYISbd1XYRBk9myaseg",
            "name": "Duckwrth",
            "type": "artist",
            "uri": "spotify:artist:0TnOYISbd1XYRBk9myaseg"
          }
      ],
      "id": "0bdNktKwMzf6d4V5BNK1KN",
      "name": "Super Bounce",
      "uri": "spotify:track:0bdNktKwMzf6d4V5BNK1KN",
      "href": "https://api.spotify.com/v1/tracks/0bdNktKwMzf6d4V5BNK1KN"
    },
    {
      "artists": [
          {
            "external_urls": {
              "spotify": "https://open.spotify.com/artist/0TnOYISbd1XYRBk9myaseg"
            },
            "href": "https://api.spotify.com/v1/artists/0TnOYISbd1XYRBk9myaseg",
            "id": "0TnOYISbd1XYRBk9myaseg",
            "name": "Daniel Caesar",
            "type": "artist",
            "uri": "spotify:artist:0TnOYISbd1XYRBk9myaseg"
          }
      ],
      "id": "1boXOL0ua7N2iCOUVI1p9F",
      "name": "Japanese Denim",
      "uri": "spotify:track:1boXOL0ua7N2iCOUVI1p9F",
      "href": "https://api.spotify.com/v1/tracks/1boXOL0ua7N2iCOUVI1p9F"
    }
  ],
  "total": 50,
  "limit": 2,
  "offset": 1,
  "previous": null,
  "href": "https://api.spotify.com/v1/me/top/artists?limit=2&offset=1",
  "next": "https://api.spotify.com/v1/me/top/artists?limit=2&offset=3"
}
 */