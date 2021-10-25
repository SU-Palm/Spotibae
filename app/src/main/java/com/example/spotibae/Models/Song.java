package com.example.spotibae.Models;

import java.util.List;

public class Song {
    private int id;
    String name;
    List<Artist> artists;
    int discNumber;
    int durationMs;
    boolean explicit;
    String href;
    boolean isLocal;
    boolean isPlayable;
    String previewUrl;
    int trackNumber;
    String type;
    String uri;
}
/* Example Song
    {
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
        "disc_number": 1,
        "duration_ms": 279440,
        "explicit": false,
        "external_urls": {
            "spotify": "https://open.spotify.com/track/2PQOX7Cz7WVT6QsDJEDhN1"
        },
        "href": "https://api.spotify.com/v1/tracks/2PQOX7Cz7WVT6QsDJEDhN1",
        "id": "2PQOX7Cz7WVT6QsDJEDhN1",
        "is_local": false,
        "is_playable": true,
        "name": "Popsnot",
        "preview_url": "https://p.scdn.co/mp3-preview/61d193c963dcf4573b235ffb5bdacf24d56d8ac9?cid=774b29d4f13844c495f206cafdad9c86",
        "track_number": 14,
        "type": "track",
        "uri": "spotify:track:2PQOX7Cz7WVT6QsDJEDhN1"
    }
    */