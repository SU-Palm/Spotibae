package com.example.spotibae.Services.Serializers;

import com.example.spotibae.Models.Album;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class AlbumDeserializer implements JsonDeserializer<Album> {
    @Override
    public Album deserialize(JsonElement json, Type typeOfT,
                             JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        JsonArray artistArray = (JsonArray) jsonObject.get("artists");
        JsonArray genresArray = (JsonArray) jsonObject.get("genres");
        JsonArray imageArray = (JsonArray) jsonObject.get("images");

        int id = jsonObject.get("id").getAsInt();
        String artistName = artistArray.get(0).getAsJsonObject().get("name").toString();
        String name = jsonObject.get("name").toString();
        String href = jsonObject.get("href").toString();
        String genre = genresArray.get(0).toString();
        String imageURI = imageArray.get(0).getAsJsonObject().get("url").toString();
        String uri = jsonObject.get("uri").toString();

        return new Album(id, artistName, name, href, genre, imageURI, uri);
    }
}

/*
{
  "album_type": "album",
  "artists": [
    {
      "external_urls": {
        "spotify": "https://open.spotify.com/artist/0wIb0PhwT3disoWykRhq6V"
      },
      "href": "https://api.spotify.com/v1/artists/0wIb0PhwT3disoWykRhq6V",
      "id": "0wIb0PhwT3disoWykRhq6V",
      "name": "Viktor Vaughn",
      "type": "artist",
      "uri": "spotify:artist:0wIb0PhwT3disoWykRhq6V"
    }
  ],
  "external_ids": {
    "upc": "888003017191"
  },
  "external_urls": {
    "spotify": "https://open.spotify.com/album/7HPjcPD2cr8E5oHvVAmBp7"
  },
  "genres": [],
  "href": "https://api.spotify.com/v1/albums/7HPjcPD2cr8E5oHvVAmBp7",
  "id": "7HPjcPD2cr8E5oHvVAmBp7",
  "images": [
    {
      "height": 640,
      "url": "https://i.scdn.co/image/ab67616d0000b273d97a55d6ace2bf9e8ccb8cb1",
      "width": 640
    }
  ],
  "label": "Sound Ink",
  "name": "Vaudeville Villain",
  "popularity": 56,
  "release_date": "2003",
  "release_date_precision": "year",
  "total_tracks": 17,
  "tracks": {
    "href": "https://api.spotify.com/v1/albums/7HPjcPD2cr8E5oHvVAmBp7/tracks?offset=0&limit=50&market=ES",
    "items": [

    ]
  },
  "type": "album",
  "uri": "spotify:album:7HPjcPD2cr8E5oHvVAmBp7"
}
 */
