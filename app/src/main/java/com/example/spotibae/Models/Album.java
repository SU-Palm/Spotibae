package com.example.spotibae.Models;

public class Album {
    public int id;
    public String artistName;
    public String name;
    public String href;
    public String genre;
    public String imageURI;
    public String uri;

    public Album() { }

    public Album(int id, String artistName, String name, String href, String genre, String imageURI, String uri) {
        this.id = id;
        this.artistName = artistName;
        this.name = name;
        this.href = href;
        this.genre = genre;
        this.imageURI = imageURI;
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Album: " + "ID: " + id + ", Artist Name: " + artistName + ", Album Name: " + name + ", Href: " + href + ", Genre: " + genre + ", Image URI: " + imageURI + ", URI: " + uri + "\n";
    }
}


/* Example
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
  "copyrights": [
    {
      "text": "(C) 2003 Sound Ink Records",
      "type": "C"
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
    },
    {
      "height": 300,
      "url": "https://i.scdn.co/image/ab67616d00001e02d97a55d6ace2bf9e8ccb8cb1",
      "width": 300
    },
    {
      "height": 64,
      "url": "https://i.scdn.co/image/ab67616d00004851d97a55d6ace2bf9e8ccb8cb1",
      "width": 64
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
        "duration_ms": 34786,
        "explicit": false,
        "external_urls": {
          "spotify": "https://open.spotify.com/track/2iOabFLR8n60Csj3kJ5s9C"
        },
        "href": "https://api.spotify.com/v1/tracks/2iOabFLR8n60Csj3kJ5s9C",
        "id": "2iOabFLR8n60Csj3kJ5s9C",
        "is_local": false,
        "is_playable": true,
        "name": "Overture",
        "preview_url": "https://p.scdn.co/mp3-preview/5b5af3b9eb98a7c3397c761ae46e51989acfbedd?cid=774b29d4f13844c495f206cafdad9c86",
        "track_number": 1,
        "type": "track",
        "uri": "spotify:track:2iOabFLR8n60Csj3kJ5s9C"
      },
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
        "duration_ms": 151773,
        "explicit": false,
        "external_urls": {
          "spotify": "https://open.spotify.com/track/6rkV9hz91Rr5iHnMRPZdEQ"
        },
        "href": "https://api.spotify.com/v1/tracks/6rkV9hz91Rr5iHnMRPZdEQ",
        "id": "6rkV9hz91Rr5iHnMRPZdEQ",
        "is_local": false,
        "is_playable": true,
        "name": "Vaudeville Villain",
        "preview_url": "https://p.scdn.co/mp3-preview/d0782a807f0b2dba6e3c9041ae7258cc1c7b0983?cid=774b29d4f13844c495f206cafdad9c86",
        "track_number": 2,
        "type": "track",
        "uri": "spotify:track:6rkV9hz91Rr5iHnMRPZdEQ"
      },
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
        "duration_ms": 164426,
        "explicit": false,
        "external_urls": {
          "spotify": "https://open.spotify.com/track/1wQixSSWydeLgkebOzu3yt"
        },
        "href": "https://api.spotify.com/v1/tracks/1wQixSSWydeLgkebOzu3yt",
        "id": "1wQixSSWydeLgkebOzu3yt",
        "is_local": false,
        "is_playable": true,
        "name": "Lickupon",
        "preview_url": "https://p.scdn.co/mp3-preview/d12d27f001c727695dfe0519bd8268ed39dcd648?cid=774b29d4f13844c495f206cafdad9c86",
        "track_number": 3,
        "type": "track",
        "uri": "spotify:track:1wQixSSWydeLgkebOzu3yt"
      },
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
        "duration_ms": 205733,
        "explicit": false,
        "external_urls": {
          "spotify": "https://open.spotify.com/track/7i8BMZK89QmgxAAkyZ46WF"
        },
        "href": "https://api.spotify.com/v1/tracks/7i8BMZK89QmgxAAkyZ46WF",
        "id": "7i8BMZK89QmgxAAkyZ46WF",
        "is_local": false,
        "is_playable": true,
        "name": "The Drop",
        "preview_url": "https://p.scdn.co/mp3-preview/a881aa3b1682b6d1f199863005f4ab2d69803ab9?cid=774b29d4f13844c495f206cafdad9c86",
        "track_number": 4,
        "type": "track",
        "uri": "spotify:track:7i8BMZK89QmgxAAkyZ46WF"
      },
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
        "duration_ms": 154586,
        "explicit": false,
        "external_urls": {
          "spotify": "https://open.spotify.com/track/63Iv8NhccFc2qXgIsrDo4Q"
        },
        "href": "https://api.spotify.com/v1/tracks/63Iv8NhccFc2qXgIsrDo4Q",
        "id": "63Iv8NhccFc2qXgIsrDo4Q",
        "is_local": false,
        "is_playable": true,
        "name": "Lactose and Lecithin",
        "preview_url": "https://p.scdn.co/mp3-preview/260c7672f20f34a453dc293e5224fbb0d8d4fb4d?cid=774b29d4f13844c495f206cafdad9c86",
        "track_number": 5,
        "type": "track",
        "uri": "spotify:track:63Iv8NhccFc2qXgIsrDo4Q"
      },
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
        "duration_ms": 235933,
        "explicit": false,
        "external_urls": {
          "spotify": "https://open.spotify.com/track/4xAdZnFbNbqGmlke315jcP"
        },
        "href": "https://api.spotify.com/v1/tracks/4xAdZnFbNbqGmlke315jcP",
        "id": "4xAdZnFbNbqGmlke315jcP",
        "is_local": false,
        "is_playable": true,
        "name": "A Dead Mouse",
        "preview_url": "https://p.scdn.co/mp3-preview/aaa25aa69902fef9f1ab67f9d8a7c41baf466dba?cid=774b29d4f13844c495f206cafdad9c86",
        "track_number": 6,
        "type": "track",
        "uri": "spotify:track:4xAdZnFbNbqGmlke315jcP"
      },
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
          },
          {
            "external_urls": {
              "spotify": "https://open.spotify.com/artist/0nLZDokTHjVWEOZOKwhi9H"
            },
            "href": "https://api.spotify.com/v1/artists/0nLZDokTHjVWEOZOKwhi9H",
            "id": "0nLZDokTHjVWEOZOKwhi9H",
            "name": "Lord Sear",
            "type": "artist",
            "uri": "spotify:artist:0nLZDokTHjVWEOZOKwhi9H"
          },
          {
            "external_urls": {
              "spotify": "https://open.spotify.com/artist/2vLoMPl90J1cisoNGX6JVY"
            },
            "href": "https://api.spotify.com/v1/artists/2vLoMPl90J1cisoNGX6JVY",
            "id": "2vLoMPl90J1cisoNGX6JVY",
            "name": "Benn Grimm",
            "type": "artist",
            "uri": "spotify:artist:2vLoMPl90J1cisoNGX6JVY"
          },
          {
            "external_urls": {
              "spotify": "https://open.spotify.com/artist/7mbHdIVGBTlLC80ngEFJAI"
            },
            "href": "https://api.spotify.com/v1/artists/7mbHdIVGBTlLC80ngEFJAI",
            "id": "7mbHdIVGBTlLC80ngEFJAI",
            "name": "Rodan",
            "type": "artist",
            "uri": "spotify:artist:7mbHdIVGBTlLC80ngEFJAI"
          },
          {
            "external_urls": {
              "spotify": "https://open.spotify.com/artist/5iwtnFL2DK4xdWeHfeOOhU"
            },
            "href": "https://api.spotify.com/v1/artists/5iwtnFL2DK4xdWeHfeOOhU",
            "id": "5iwtnFL2DK4xdWeHfeOOhU",
            "name": "Louis Logic",
            "type": "artist",
            "uri": "spotify:artist:5iwtnFL2DK4xdWeHfeOOhU"
          }
        ],
        "disc_number": 1,
        "duration_ms": 249333,
        "explicit": false,
        "external_urls": {
          "spotify": "https://open.spotify.com/track/4dTHs0vspyN5eSzgNRlDrv"
        },
        "href": "https://api.spotify.com/v1/tracks/4dTHs0vspyN5eSzgNRlDrv",
        "id": "4dTHs0vspyN5eSzgNRlDrv",
        "is_local": false,
        "is_playable": true,
        "name": "Open Mic Nite, Pt. 1 (feat. Lord Sear, Benn Grimm as Brother Sambuca, Rodan as Dr. Moraue, & Louis Logic as Himself)",
        "preview_url": "https://p.scdn.co/mp3-preview/7216b365ca3ed2da79a5818ec3023e553be1c998?cid=774b29d4f13844c495f206cafdad9c86",
        "track_number": 7,
        "type": "track",
        "uri": "spotify:track:4dTHs0vspyN5eSzgNRlDrv"
      },
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
        "duration_ms": 180600,
        "explicit": false,
        "external_urls": {
          "spotify": "https://open.spotify.com/track/2xIUHf9uxG5bImKG6hNrMv"
        },
        "href": "https://api.spotify.com/v1/tracks/2xIUHf9uxG5bImKG6hNrMv",
        "id": "2xIUHf9uxG5bImKG6hNrMv",
        "is_local": false,
        "is_playable": true,
        "name": "Raedawn",
        "preview_url": "https://p.scdn.co/mp3-preview/bb719fc3b8dca4ee12d0486da51d53a4fb091b6f?cid=774b29d4f13844c495f206cafdad9c86",
        "track_number": 8,
        "type": "track",
        "uri": "spotify:track:2xIUHf9uxG5bImKG6hNrMv"
      },
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
          },
          {
            "external_urls": {
              "spotify": "https://open.spotify.com/artist/2BHRu1QeMRt12BsLArt3gE"
            },
            "href": "https://api.spotify.com/v1/artists/2BHRu1QeMRt12BsLArt3gE",
            "id": "2BHRu1QeMRt12BsLArt3gE",
            "name": "Apani B",
            "type": "artist",
            "uri": "spotify:artist:2BHRu1QeMRt12BsLArt3gE"
          }
        ],
        "disc_number": 1,
        "duration_ms": 267440,
        "explicit": false,
        "external_urls": {
          "spotify": "https://open.spotify.com/track/4i4P1ACAeWcyJTxQSd0prx"
        },
        "href": "https://api.spotify.com/v1/tracks/4i4P1ACAeWcyJTxQSd0prx",
        "id": "4i4P1ACAeWcyJTxQSd0prx",
        "is_local": false,
        "is_playable": true,
        "name": "Can I Watch? (feat. Apani B as Nikki)",
        "preview_url": "https://p.scdn.co/mp3-preview/1033dc5594056d150b5033a6028a1222504349a1?cid=774b29d4f13844c495f206cafdad9c86",
        "track_number": 9,
        "type": "track",
        "uri": "spotify:track:4i4P1ACAeWcyJTxQSd0prx"
      },
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
        "duration_ms": 148826,
        "explicit": false,
        "external_urls": {
          "spotify": "https://open.spotify.com/track/1pXvZZ7Ubwt4rpDt3JEHdY"
        },
        "href": "https://api.spotify.com/v1/tracks/1pXvZZ7Ubwt4rpDt3JEHdY",
        "id": "1pXvZZ7Ubwt4rpDt3JEHdY",
        "is_local": false,
        "is_playable": true,
        "name": "Saliva",
        "preview_url": "https://p.scdn.co/mp3-preview/26fa63c0c7550109c4d19892fde53e6564b49fed?cid=774b29d4f13844c495f206cafdad9c86",
        "track_number": 10,
        "type": "track",
        "uri": "spotify:track:1pXvZZ7Ubwt4rpDt3JEHdY"
      },
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
        "duration_ms": 163786,
        "explicit": false,
        "external_urls": {
          "spotify": "https://open.spotify.com/track/5oSQQjaU1JzbNuaiYSKzyg"
        },
        "href": "https://api.spotify.com/v1/tracks/5oSQQjaU1JzbNuaiYSKzyg",
        "id": "5oSQQjaU1JzbNuaiYSKzyg",
        "is_local": false,
        "is_playable": true,
        "name": "A Modern Day Mugging",
        "preview_url": "https://p.scdn.co/mp3-preview/49f27d03612841f9d3df9fa4ec3fb618158b7187?cid=774b29d4f13844c495f206cafdad9c86",
        "track_number": 11,
        "type": "track",
        "uri": "spotify:track:5oSQQjaU1JzbNuaiYSKzyg"
      },
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
          },
          {
            "external_urls": {
              "spotify": "https://open.spotify.com/artist/0nLZDokTHjVWEOZOKwhi9H"
            },
            "href": "https://api.spotify.com/v1/artists/0nLZDokTHjVWEOZOKwhi9H",
            "id": "0nLZDokTHjVWEOZOKwhi9H",
            "name": "Lord Sear",
            "type": "artist",
            "uri": "spotify:artist:0nLZDokTHjVWEOZOKwhi9H"
          },
          {
            "external_urls": {
              "spotify": "https://open.spotify.com/artist/0VKZnWlBYf8TbIdDfUdcMc"
            },
            "href": "https://api.spotify.com/v1/artists/0VKZnWlBYf8TbIdDfUdcMc",
            "id": "0VKZnWlBYf8TbIdDfUdcMc",
            "name": "Hydro",
            "type": "artist",
            "uri": "spotify:artist:0VKZnWlBYf8TbIdDfUdcMc"
          }
        ],
        "disc_number": 1,
        "duration_ms": 193320,
        "explicit": false,
        "external_urls": {
          "spotify": "https://open.spotify.com/track/5XRO5KnH7OvMPElYl8QYkD"
        },
        "href": "https://api.spotify.com/v1/tracks/5XRO5KnH7OvMPElYl8QYkD",
        "id": "5XRO5KnH7OvMPElYl8QYkD",
        "is_local": false,
        "is_playable": true,
        "name": "Open Mic Nite, Pt. 2 (feat. Lord Sear & Hydro)",
        "preview_url": "https://p.scdn.co/mp3-preview/60c0d0b5990903d3f99b750bcf204c9a9400a7b8?cid=774b29d4f13844c495f206cafdad9c86",
        "track_number": 12,
        "type": "track",
        "uri": "spotify:track:5XRO5KnH7OvMPElYl8QYkD"
      },
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
          },
          {
            "external_urls": {
              "spotify": "https://open.spotify.com/artist/4WKyQba1T0Y5D8R5SkfZx4"
            },
            "href": "https://api.spotify.com/v1/artists/4WKyQba1T0Y5D8R5SkfZx4",
            "id": "4WKyQba1T0Y5D8R5SkfZx4",
            "name": "M. Sayyid",
            "type": "artist",
            "uri": "spotify:artist:4WKyQba1T0Y5D8R5SkfZx4"
          }
        ],
        "disc_number": 1,
        "duration_ms": 207373,
        "explicit": false,
        "external_urls": {
          "spotify": "https://open.spotify.com/track/29XBHZKdcXAg8deuEVyoeI"
        },
        "href": "https://api.spotify.com/v1/tracks/29XBHZKdcXAg8deuEVyoeI",
        "id": "29XBHZKdcXAg8deuEVyoeI",
        "is_local": false,
        "is_playable": true,
        "name": "Never Dead (feat. M. Sayyid as Curis Strifer)",
        "preview_url": "https://p.scdn.co/mp3-preview/f4dc9cc960769f953753e5bb0d6da0d5094f07b2?cid=774b29d4f13844c495f206cafdad9c86",
        "track_number": 13,
        "type": "track",
        "uri": "spotify:track:29XBHZKdcXAg8deuEVyoeI"
      },
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
      },
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
        "duration_ms": 133666,
        "explicit": false,
        "external_urls": {
          "spotify": "https://open.spotify.com/track/4fA40676Ja1N59rCa9QECx"
        },
        "href": "https://api.spotify.com/v1/tracks/4fA40676Ja1N59rCa9QECx",
        "id": "4fA40676Ja1N59rCa9QECx",
        "is_local": false,
        "is_playable": true,
        "name": "Mr. Clean",
        "preview_url": "https://p.scdn.co/mp3-preview/c671aab4238f99fc98623e558a075e1b7f7f926f?cid=774b29d4f13844c495f206cafdad9c86",
        "track_number": 15,
        "type": "track",
        "uri": "spotify:track:4fA40676Ja1N59rCa9QECx"
      },
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
        "duration_ms": 213080,
        "explicit": false,
        "external_urls": {
          "spotify": "https://open.spotify.com/track/1UXAFsGY9uCq0SYKtF112T"
        },
        "href": "https://api.spotify.com/v1/tracks/1UXAFsGY9uCq0SYKtF112T",
        "id": "1UXAFsGY9uCq0SYKtF112T",
        "is_local": false,
        "is_playable": true,
        "name": "G.M.C.",
        "preview_url": "https://p.scdn.co/mp3-preview/23045e58690e44f14bc288c54487dc7d8e2a9758?cid=774b29d4f13844c495f206cafdad9c86",
        "track_number": 16,
        "type": "track",
        "uri": "spotify:track:1UXAFsGY9uCq0SYKtF112T"
      },
      {
        "artists": [
          {
            "external_urls": {
              "spotify": "https://open.spotify.com/artist/2pAWfrd7WFF3XhVt9GooDL"
            },
            "href": "https://api.spotify.com/v1/artists/2pAWfrd7WFF3XhVt9GooDL",
            "id": "2pAWfrd7WFF3XhVt9GooDL",
            "name": "MF DOOM",
            "type": "artist",
            "uri": "spotify:artist:2pAWfrd7WFF3XhVt9GooDL"
          }
        ],
        "disc_number": 1,
        "duration_ms": 415453,
        "explicit": false,
        "external_urls": {
          "spotify": "https://open.spotify.com/track/1VIUe3bUgdCb1VbJUZxTbf"
        },
        "href": "https://api.spotify.com/v1/tracks/1VIUe3bUgdCb1VbJUZxTbf",
        "id": "1VIUe3bUgdCb1VbJUZxTbf",
        "is_local": false,
        "is_playable": true,
        "name": "Untitled A.K.A. Change the Beat (Bonus Track)",
        "preview_url": "https://p.scdn.co/mp3-preview/83ca94d16d2c8462c555446aa63538207a5c2a20?cid=774b29d4f13844c495f206cafdad9c86",
        "track_number": 17,
        "type": "track",
        "uri": "spotify:track:1VIUe3bUgdCb1VbJUZxTbf"
      }
    ],
    "limit": 50,
    "next": null,
    "offset": 0,
    "previous": null,
    "total": 17
  },
  "type": "album",
  "uri": "spotify:album:7HPjcPD2cr8E5oHvVAmBp7"
}
*/