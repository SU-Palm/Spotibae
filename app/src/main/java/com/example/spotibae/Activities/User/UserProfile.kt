package com.example.spotibae.Activities.User

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import android.content.Intent
import android.graphics.*
import com.spotify.android.appremote.api.SpotifyAppRemote
import android.os.Bundle
import com.example.spotibae.R
import com.google.firebase.database.FirebaseDatabase
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import android.provider.MediaStore
import android.widget.Toast
import com.example.spotibae.Activities.Welcome.BaseActivity
import com.example.spotibae.Activities.User.Settings.ChangePhoneNumber
import com.example.spotibae.Activities.User.Settings.ChangeDistance
import com.example.spotibae.Activities.User.Settings.ChangeGenderMatchPreference
import com.example.spotibae.Activities.User.Settings.ChangeBio
import com.example.spotibae.Activities.User.Settings.ChangeGender
import com.example.spotibae.Activities.User.Settings.ChangeName
import com.example.spotibae.Activities.User.Settings.ChangeAgePreference
import com.example.spotibae.Activities.User.Settings.ChangeLocation
import com.squareup.picasso.Picasso
import com.example.spotibae.Activities.Welcome.WelcomeScreen
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector.ConnectionListener
import kotlin.Throws
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.AuthorizationClient
import com.example.spotibae.Models.Song
import com.example.spotibae.Services.Serializers.ArtistDeserializer
import com.example.spotibae.Services.Serializers.SongDeserializer
import android.net.Uri
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.example.spotibae.BuildConfig
import com.example.spotibae.Models.Artist
import com.google.gson.*
import com.squareup.picasso.Transformation
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.ArrayList
import java.util.HashMap

class UserProfile : AppCompatActivity() {
    // Spotify Remote Auth
    private val CLIENT_ID = BuildConfig.CLIENT_ID
    private val REDIRECT_URI = "http://com.example.spotibae/callback"

    // Spotify Auth
    val AUTH_TOKEN_REQUEST_CODE = 0x10
    val AUTH_CODE_REQUEST_CODE = 0x11

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit private var mDatabase: DatabaseReference
    var storage = FirebaseStorage.getInstance()
    private lateinit var storageRef: StorageReference
    private lateinit var profileRef: StorageReference

    private lateinit var doneButton: TextView
    private lateinit var signOutButton: Button
    private lateinit var phoneNumberButton: Button
    private lateinit var distanceButton: Button
    private lateinit var genderPrefButton: Button
    private lateinit var bioButton: Button
    private lateinit var genderButton: Button
    private lateinit var nameButton: Button
    private lateinit var agePrefButton: Button
    private lateinit var verifySpotifyButton: Button
    private lateinit var locationButton: Button
    private lateinit var passwordResetButton: Button
    private lateinit var profileName: TextView
    private lateinit var userEmailAddress: TextView
    private lateinit var userPhoneNumber: TextView
    private lateinit var userLocation: TextView
    private lateinit var userDistance: TextView
    private lateinit var userAgePref: TextView
    private lateinit var genderPrefText: TextView
    private lateinit var userBio: TextView
    private lateinit var userName: TextView
    private lateinit var userGender: TextView

    // Uploading image and other stuff
    private lateinit var filepath: Uri
    private val PICK_IMAGE_REQUEST = -1
    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null
    private lateinit var profilePic: ImageView
    private lateinit var uploadPicButton: ImageView

    // Keys for persistent storage
    private val EMAIL_KEY = "email"
    private val PHONE_NUM_KEY = "phone"
    private val LOCATION_KEY = "location"
    private val DISTANCE_KEY = "distance"
    private val AGE_PREF_KEY = "age"
    private val SHOW_ME_KEY = "showMe"
    private val BIO_KEY = "bio"
    private val NAME_KEY = "name"
    private val GENDER_KEY = "gender"
    private var mSpotifyAppRemote: SpotifyAppRemote? = null
    private val mOkHttpClient = OkHttpClient()
    private var mAccessToken: String? = null
    private var mAccessCode: String? = null
    private var mCall: Call? = null
    private lateinit var testArtistJson: String
    private lateinit var testSongJson: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        // Initializing
        setViews()
        setListeners()
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData")
        storageRef = storage.reference
        if (savedInstanceState != null) {
            profileName.text = savedInstanceState.getString(NAME_KEY)
            userEmailAddress.text = savedInstanceState.getString(EMAIL_KEY)
            userPhoneNumber.text = savedInstanceState.getString(PHONE_NUM_KEY)
            userLocation.text = savedInstanceState.getString(LOCATION_KEY)
            userDistance.text = savedInstanceState.getString(DISTANCE_KEY)
            userAgePref.text = savedInstanceState.getString(AGE_PREF_KEY)
            genderPrefText.text = savedInstanceState.getString(SHOW_ME_KEY)
            userBio.text = savedInstanceState.getString(BIO_KEY)
            userName.text = savedInstanceState.getString(NAME_KEY)
            userGender.text = savedInstanceState.getString(GENDER_KEY)
        } else {
            initData()
        }

        // Callback for picking image in gallery
        activityResultLauncher = registerForActivityResult(
            StartActivityForResult()
        ) { result ->
            println("Result Code: " + result.resultCode + " ")
            if (result.resultCode == PICK_IMAGE_REQUEST) {
                filepath = result.data!!.data!!
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)
                    profilePic.setImageBitmap(
                        Bitmap.createScaledBitmap(
                            getCroppedBitmap(bitmap),
                            350,
                            400,
                            true
                        )
                    )
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    profileRef =
                        storageRef.child("User").child(userEmailAddress.text.toString())
                            .child("profilePic.png")
                    val uploadTask = profileRef.putBytes(data)

                    uploadTask.addOnFailureListener {
                        // Handle unsuccessful uploads
                    }
                        .addOnSuccessListener {
                            val user = mAuth.currentUser
                            val uId = user!!.uid
                            FirebaseDatabase.getInstance().getReference("UserData").child(uId)
                                .child("firstLogin").setValue(false)
                        }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        testArtistJson = """{
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
}"""
        testSongJson = """{
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
}"""
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EMAIL_KEY, userEmailAddress.text.toString())
        outState.putString(PHONE_NUM_KEY, userPhoneNumber.text.toString())
        outState.putString(LOCATION_KEY, userLocation.text.toString())
        outState.putString(DISTANCE_KEY, userDistance.text.toString())
        outState.putString(AGE_PREF_KEY, userAgePref.text.toString())
        outState.putString(SHOW_ME_KEY, genderPrefText.text.toString())
        outState.putString(BIO_KEY, userBio.text.toString())
        outState.putString(NAME_KEY, userName.text.toString())
        outState.putString(GENDER_KEY, userGender.text.toString())
        super.onSaveInstanceState(outState)
    }

    fun setImage(email: String) {
        val storageReference = FirebaseStorage.getInstance().reference
        val photoReference = storageReference.child("User").child(email).child("profilePic.png")
        val ONE_MEGABYTE = (1024 * 1024).toLong()
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            profilePic.setImageBitmap(
                Bitmap.createScaledBitmap(
                    getCroppedBitmap(bitmap),
                    350,
                    350,
                    true
                )
            )
        }.addOnFailureListener {
            Toast.makeText(
                applicationContext,
                "No Such file or Path found",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getCroppedBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(
            (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(), (
                    bitmap.width / 2).toFloat(), paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        activityResultLauncher?.launch(Intent.createChooser(intent, "Select Picture"))
    }

    private fun setListeners() {
        doneButton.setOnClickListener {
            val intent = Intent(this, BaseActivity::class.java)
            val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
            intent.putExtra("FRAGMENT_SELECTED", fragSelected)
            intent.putExtra("PROFILE_VAR", "PROFILE")
            startActivity(intent)
        }
        signOutButton.setOnClickListener { signOut() }
        phoneNumberButton.setOnClickListener {
            val intent = Intent(this, ChangePhoneNumber::class.java)
            val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
            intent.putExtra("FRAGMENT_SELECTED", fragSelected)
            startActivity(intent)
        }
        distanceButton.setOnClickListener {
            val intent = Intent(this, ChangeDistance::class.java)
            val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
            intent.putExtra("FRAGMENT_SELECTED", fragSelected)
            intent.putExtra("CURRENT_DISTANCE", userDistance.text.toString())
            startActivity(intent)
        }
        genderPrefButton.setOnClickListener {
            val intent = Intent(this, ChangeGenderMatchPreference::class.java)
            val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
            intent.putExtra("FRAGMENT_SELECTED", fragSelected)
            startActivity(intent)
        }
        bioButton.setOnClickListener {
            val intent = Intent(this, ChangeBio::class.java)
            val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
            intent.putExtra("FRAGMENT_SELECTED", fragSelected)
            startActivity(intent)
        }
        genderButton.setOnClickListener {
            val intent = Intent(this, ChangeGender::class.java)
            val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
            intent.putExtra("FRAGMENT_SELECTED", fragSelected)
            startActivity(intent)
        }
        nameButton.setOnClickListener {
            val intent = Intent(this, ChangeName::class.java)
            val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
            intent.putExtra("FRAGMENT_SELECTED", fragSelected)
            startActivity(intent)
        }
        agePrefButton.setOnClickListener {
            val intent = Intent(this, ChangeAgePreference::class.java)
            val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
            intent.putExtra("FRAGMENT_SELECTED", fragSelected)
            startActivity(intent)
        }
        uploadPicButton.setOnClickListener { chooseImage() }
        verifySpotifyButton.setOnClickListener { authenticateAppRemoteSpotify() }
        locationButton.setOnClickListener {
            val intent = Intent(this, ChangeLocation::class.java)
            val fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString()
            intent.putExtra("FRAGMENT_SELECTED", fragSelected)
            startActivity(intent)
        }
        passwordResetButton.setOnClickListener { passwordReset() }
    }

    private fun setViews() {
        // TextViews
        profileName = findViewById(R.id.profileName)
        userEmailAddress = findViewById(R.id.userEmailAddress)
        userPhoneNumber = findViewById(R.id.userPhoneNumber)
        userLocation = findViewById(R.id.userLocation)
        userDistance = findViewById(R.id.userDistance)
        userAgePref = findViewById(R.id.userAgePref)
        genderPrefText = findViewById(R.id.genderPref)
        userBio = findViewById(R.id.userBio)
        userName = findViewById(R.id.userName)
        userGender = findViewById(R.id.userGender)

        // Buttons
        nameButton = findViewById(R.id.nameButton)
        agePrefButton = findViewById(R.id.ageButton)
        genderButton = findViewById(R.id.genderButton)
        bioButton = findViewById(R.id.bioButton)
        genderPrefButton = findViewById(R.id.preferenceButton)
        distanceButton = findViewById(R.id.distanceButton)
        phoneNumberButton = findViewById(R.id.phoneNumberButton)
        signOutButton = findViewById(R.id.signOut)
        doneButton = findViewById(R.id.done)
        uploadPicButton = findViewById(R.id.uploadPicButton)
        verifySpotifyButton = findViewById(R.id.connectSpotify)
        locationButton = findViewById(R.id.locationButton)
        passwordResetButton = findViewById(R.id.resetPassword)

        //ImageView
        profilePic = findViewById(R.id.profilePic)
    }

    fun initData() {
        val user = mAuth.currentUser
        val uId = user!!.uid
        mDatabase.child(uId).get().addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("firebase", "Error getting data", task.exception)
            } else {
                Log.d("firebase User", task.result.value.toString())
                val user = task.result.value as HashMap<*, *>
                val firstName = user["firstName"].toString()
                val email = user["email"].toString()
                val fullName = user["fullName"].toString()
                val bio = user["bio"].toString()
                val gender = user["gender"].toString()
                val phoneNumber = user["phoneNumber"].toString()
                val distance = user["distance"] as Long
                val lowestAgePref = user["lowestAgePref"] as Long
                val highestAgePref = user["highestAgePref"] as Long
                val genderPref = user["genderPref"].toString()
                val location = user["location"].toString()
                profileName.text = firstName
                userEmailAddress.text = email
                userPhoneNumber.text = phoneNumber
                userLocation.text = location
                userDistance.text = distance.toString()
                userAgePref.text = "$lowestAgePref - $highestAgePref"
                genderPrefText.text = genderPref
                userBio.text = bio
                userName.text = fullName
                userGender.text = gender
                val firstLogin = java.lang.Boolean.parseBoolean(user["firstLogin"].toString())
                if (firstLogin) {
                    Picasso.get().load(R.drawable.defaultprofile).centerCrop().resize(350, 350)
                        .transform(
                            CircleTransform()
                        ).into(profilePic)
                } else {
                    setImage(email)
                }
            }
        }
    }

    private fun signOut() {
        mAuth.signOut()
        val intent = Intent(this, WelcomeScreen::class.java)
        this.startActivity(intent)
    }

    private fun passwordReset() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(userEmailAddress.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("MainActivity", "Email sent.")
                }
            }
    }

    private fun authenticateAppRemoteSpotify() {
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()
        SpotifyAppRemote.connect(this, connectionParams,
            object : ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote
                    Log.d("MainActivity", "Connected! Yay!")
                    // Now you can start interacting with App Remote
                    val user = mAuth.currentUser
                    val uid = user!!.uid
                    mDatabase.child(uid).child("spotifyVerified").setValue(true)
                    try {
                        authenticateAuthSpotify()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MainActivity", throwable.message, throwable)

                    // Something went wrong when attempting to connect! Handle errors here
                }
            })
    }

    // Spotify Stuff
    @Throws(InterruptedException::class)
    private fun authenticateAuthSpotify() {
        userRequestToken()
    }

    private fun userRequestToken() {
        val request = AuthorizationResponse.Type.TOKEN.getAuthenticationRequest()
        AuthorizationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request)
    }

    private fun AuthorizationResponse.Type.getAuthenticationRequest(): AuthorizationRequest {
        return AuthorizationRequest.Builder(CLIENT_ID, this, REDIRECT_URI)
            .setShowDialog(false)
            .setScopes(arrayOf("user-read-email", "user-top-read"))
            .setCampaign("your-campaign-token")
            .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val response = AuthorizationClient.getResponse(resultCode, data)
        if (response.error != null && response.error.isNotEmpty()) {
            Log.d("Spotify Auth", "Failed")
        }
        if (requestCode == AUTH_TOKEN_REQUEST_CODE) {
            mAccessToken = response.accessToken
            try {
                onGetUserTopTracks()
                Thread.sleep(500)
                onGetUserTopArtists()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        } else if (requestCode == AUTH_CODE_REQUEST_CODE) {
            mAccessCode = response.code
            try {
                onGetUserTopTracks()
                Thread.sleep(500)
                onGetUserTopArtists()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    @Throws(InterruptedException::class)
    fun onGetUserTopTracks() {
        if (mAccessToken == null) {
            Log.d("Spotify Auth", "Failed at onGetUserProfileClicked()")
            return
        }
        val requestTopTracks = Request.Builder()
            .url("https://api.spotify.com/v1/me/top/tracks?time_range=medium_term&limit=5&offset=1")
            .addHeader("Authorization", "Bearer $mAccessToken")
            .addHeader("Content-Type", "application/json")
            .build()
        cancelCall()
        mCall = mOkHttpClient.newCall(requestTopTracks)
        mCall?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val jsonObject = JsonParser().parse(
                    response.body()!!.string()
                ).asJsonObject
                //JsonObject jsonObject = new JsonParser().parse(testSongJson).getAsJsonObject();
                val songArray = jsonObject.getAsJsonArray("items")
                val songList = createListSongs(songArray)
                addUserFavoriteSongsToFirebase(songList)
            }
        })
    }

    @Throws(InterruptedException::class)
    fun onGetUserTopArtists() {
        if (mAccessToken == null) {
            Log.d("Spotify Auth", "Failed at onGetUserProfileClicked()")
            return
        }
        val requestTopTracks = Request.Builder()
            .url("https://api.spotify.com/v1/me/top/artists?time_range=medium_term&limit=5&offset=1")
            .addHeader("Authorization", "Bearer $mAccessToken")
            .addHeader("Content-Type", "application/json")
            .build()
        Thread.sleep(500)
        cancelCall()
        mCall = mOkHttpClient.newCall(requestTopTracks)
        mCall?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val jsonObject = JsonParser().parse(
                    response.body()!!.string()
                ).asJsonObject
                //JsonObject jsonObject = new JsonParser().parse(testArtistJson).getAsJsonObject();
                val artistArray = jsonObject.getAsJsonArray("items")
                val artistList = createListArtists(artistArray)
                addUserFavoriteArtistsToFirebase(artistList)
            }
        })
    }

    private fun cancelCall() {
        if (mCall != null) {
            mCall?.cancel()
        }
    }

    fun addUserFavoriteSongsToFirebase(songs: List<Song>) {
        val hashHashSongs: MutableMap<String, Map<String, String>> = HashMap()
        for (song in songs) {
            val hashSongs: MutableMap<String, String> = HashMap()
            hashSongs["id"] = song.id
            hashSongs["name"] = song.name
            hashSongs["href"] = song.href
            hashSongs["artistName"] = song.artistName
            hashSongs["uri"] = song.uri
            hashHashSongs[song.id] = hashSongs
        }
        val user = mAuth.currentUser
        if(user != null) {
            val uId = user.uid
            mDatabase.child(uId).child("favoriteSongs").setValue(hashHashSongs)
        }
    }

    fun addUserFavoriteArtistsToFirebase(artists: List<Artist>) {
        val hashHashArtists: MutableMap<String, Map<String, String>> = HashMap()
        for (artist in artists) {
            val hashArtists: MutableMap<String, String> = HashMap()
            hashArtists["id"] = artist.id
            hashArtists["name"] = artist.name
            hashArtists["genre"] = artist.genre
            hashArtists["href"] = artist.href
            hashArtists["imageURI"] = artist.imageURI
            hashArtists["uri"] = artist.uri
            hashHashArtists[artist.id] = hashArtists
        }
        val user = mAuth.currentUser
        if(user != null) {
            val uId = user.uid
            mDatabase.child(uId).child("favoriteArtists").setValue(hashHashArtists)
        }
    }

    private fun createArtist(json: JsonElement?): Artist {
        val gson = GsonBuilder()
            .registerTypeAdapter(
                Artist::class.java,
                ArtistDeserializer()
            )
            .create()
        return gson.fromJson(json, Artist::class.java)
    }

    private fun createSong(json: JsonElement?): Song {
        val gson = GsonBuilder()
            .registerTypeAdapter(Song::class.java, SongDeserializer())
            .create()
        return gson.fromJson(json, Song::class.java)
    }

    fun createListArtists(itemArray: JsonArray): List<Artist> {
        val artistList: MutableList<Artist> = ArrayList()
        for (i in 0..4) {
            val json = itemArray[i].asJsonObject
            val artist = createArtist(json)
            artistList.add(artist)
        }
        return artistList
    }

    fun createListSongs(itemArray: JsonArray): List<Song> {
        val songList: MutableList<Song> = ArrayList()
        for (i in 0..4) {
            val json = itemArray[i].asJsonObject
            val song = createSong(json)
            songList.add(song)
        }
        return songList
    }

    inner class CircleTransform : Transformation {
        override fun transform(source: Bitmap): Bitmap {
            val size = Math.min(source.width, source.height)
            val x = (source.width - size) / 2
            val y = (source.height - size) / 2
            val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
            if (squaredBitmap != source) {
                source.recycle()
            }
            val bitmap = Bitmap.createBitmap(size, size, source.config)
            val canvas = Canvas(bitmap)
            val paint = Paint()
            val shader = BitmapShader(
                squaredBitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP
            )
            paint.shader = shader
            paint.isAntiAlias = true
            val r = size / 2f
            canvas.drawCircle(r, r, r, paint)
            squaredBitmap.recycle()
            return bitmap
        }

        override fun key(): String {
            return "circle"
        }
    }
}