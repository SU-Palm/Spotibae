package com.example.spotibae.Models;

public class Artist {
    public String id;
    public String name;
    public String href;
    public String genre;
    public String imageURI;
    public String uri;

    public Artist() { }

    public Artist(String id, String name, String href, String genre, String imageURI, String uri) {
        this.id = id;
        this.name = name;
        this.href = href;
        this.genre = genre;
        this.imageURI = imageURI;
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Artist: " + "ID: " + id + ", Name: " + name + ", Href: " + href + ", Genre: " + genre + ", Image URI: " + imageURI + ", URI: " + uri + "\n";
    }
}
/*
{
  "items": [
    {
      "external_urls": {
        "spotify": "https://open.spotify.com/artist/5K4W6rqBFWDnAN6FQUkS6x"
      },
      "followers": {
        "href": null,
        "total": 15547857
      },
      "genres": [
        "chicago rap",
        "rap"
      ],
      "href": "https://api.spotify.com/v1/artists/5K4W6rqBFWDnAN6FQUkS6x",
      "id": "5K4W6rqBFWDnAN6FQUkS6x",
      "images": [
        {
          "height": 640,
          "url": "https://i.scdn.co/image/ab6761610000e5eb867008a971fae0f4d913f63a",
          "width": 640
        },
        {
          "height": 320,
          "url": "https://i.scdn.co/image/ab67616100005174867008a971fae0f4d913f63a",
          "width": 320
        },
        {
          "height": 160,
          "url": "https://i.scdn.co/image/ab6761610000f178867008a971fae0f4d913f63a",
          "width": 160
        }
      ],
      "name": "Kanye West",
      "popularity": 95,
      "type": "artist",
      "uri": "spotify:artist:5K4W6rqBFWDnAN6FQUkS6x"
    },
    {
      "external_urls": {
        "spotify": "https://open.spotify.com/artist/3v0QTRruILayLe5VsaYdvk"
      },
      "followers": {
        "href": null,
        "total": 173900
      },
      "genres": [
        "aesthetic rap",
        "dark trap",
        "drift phonk"
      ],
      "href": "https://api.spotify.com/v1/artists/3v0QTRruILayLe5VsaYdvk",
      "id": "3v0QTRruILayLe5VsaYdvk",
      "images": [
        {
          "height": 640,
          "url": "https://i.scdn.co/image/ab6761610000e5eb74ca6577ea876272d6f59225",
          "width": 640
        },
        {
          "height": 320,
          "url": "https://i.scdn.co/image/ab6761610000517474ca6577ea876272d6f59225",
          "width": 320
        },
        {
          "height": 160,
          "url": "https://i.scdn.co/image/ab6761610000f17874ca6577ea876272d6f59225",
          "width": 160
        }
      ],
      "name": "Haarper",
      "popularity": 68,
      "type": "artist",
      "uri": "spotify:artist:3v0QTRruILayLe5VsaYdvk"
    },
    {
      "external_urls": {
        "spotify": "https://open.spotify.com/artist/3v0QTRruILayLe5VsaYdvk"
      },
      "followers": {
        "href": null,
        "total": 173900
      },
      "genres": [
        "aesthetic rap",
        "dark trap",
        "drift phonk"
      ],
      "href": "https://api.spotify.com/v1/artists/3v0QTRruILayLe5VsaYdvk",
      "id": "3v0QTRruILayLe5Vsatvvv",
      "images": [
        {
          "height": 640,
          "url": "https://i.scdn.co/image/ab67616d0000b2731e340d1480e7bb29a45e3bd7",
          "width": 640
        },
        {
          "height": 320,
          "url": "https://i.scdn.co/image/ab6761610000517474ca6577ea876272d6f59225",
          "width": 320
        },
        {
          "height": 160,
          "url": "https://i.scdn.co/image/ab6761610000f17874ca6577ea876272d6f59225",
          "width": 160
        }
      ],
      "name": "Pitbull",
      "popularity": 68,
      "type": "artist",
      "uri": "spotify:artist:3v0QTRruILayLe5VsaYdvk"
    },
    {
      "external_urls": {
        "spotify": "https://open.spotify.com/artist/3v0QTRruILayLe5VsaYdvk"
      },
      "followers": {
        "href": null,
        "total": 173900
      },
      "genres": [
        "aesthetic rap",
        "dark trap",
        "drift phonk"
      ],
      "href": "https://api.spotify.com/v1/artists/3v0QTRruILayLe5VsaYdvk",
      "id": "3v0QTRruILayLe5VsaYjjq",
      "images": [
        {
          "height": 640,
          "url": "https://i.scdn.co/image/ab67616d0000b27346f07fa4f28bf824840ddacb",
          "width": 640
        },
        {
          "height": 320,
          "url": "https://i.scdn.co/image/ab6761610000517474ca6577ea876272d6f59225",
          "width": 320
        },
        {
          "height": 160,
          "url": "https://i.scdn.co/image/ab6761610000f17874ca6577ea876272d6f59225",
          "width": 160
        }
      ],
      "name": "Brockhampton",
      "popularity": 68,
      "type": "artist",
      "uri": "spotify:artist:3v0QTRruILayLe5VsaYdvk"
    },
  ],
  "total": 50,
  "limit": 4,
  "offset": 1,
  "previous": null,
  "href": "https://api.spotify.com/v1/me/top/artists?limit=2&offset=1",
  "next": "https://api.spotify.com/v1/me/top/artists?limit=2&offset=3"
}


*/