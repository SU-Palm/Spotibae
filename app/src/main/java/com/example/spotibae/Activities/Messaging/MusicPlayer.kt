package com.example.spotibae.Activities.Messaging

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.database.ValueEventListener
import android.os.Bundle
import com.example.spotibae.R
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import android.graphics.*
import com.example.spotibae.Activities.Welcome.BaseActivity
import com.google.firebase.storage.StorageReference
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.OnFailureListener
import com.spotify.android.appremote.api.ConnectionParams
import com.example.spotibae.Activities.Messaging.MusicPlayer
import com.spotify.android.appremote.api.Connector.ConnectionListener
import com.spotify.protocol.types.PlayerState
import com.squareup.picasso.Picasso
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.spotibae.BuildConfig
import com.spotify.protocol.types.Track
import java.lang.StringBuilder
import java.util.*
import java.util.concurrent.TimeUnit

class MusicPlayer : AppCompatActivity() {
    private val CLIENT_ID = BuildConfig.CLIENT_ID
    private val REDIRECT_URI = "http://com.example.spotibae/callback"
    private lateinit var songImage: ImageView
    private lateinit var timeElapsed: TextView
    private lateinit var songName: TextView
    private lateinit var artistName: TextView
    private lateinit var playPauseButton: ImageView
    private lateinit var skipForwardButton: ImageView
    private lateinit var skipBackwardButton: ImageView
    private lateinit var userProfilePic: ImageView
    private lateinit var userNameText: TextView
    private var onOff = true
    private lateinit var userName: String
    private lateinit var userFirebaseId: String
    private lateinit var userEmail: String
    private var songDuration: Long = 0
    private var timeElapsedCode: Long = 0
    private lateinit var mSpotifyAppRemote: SpotifyAppRemote
    private var i = 0
    private var bothInRoom = true
    private var oneInRoom = true
    private lateinit var spotifyTrack: Track
    private lateinit var backButton: ImageView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private var storage = FirebaseStorage.getInstance()
    private lateinit var mSearchedLocationReferenceListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("RoomData")
        getDataFromUser(savedInstanceState)
        setViews()
        setListeners()
        userNameText.text = userName
        setImage(userEmail)
    }

    private fun setTime(time: Long) {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(time)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60
        if (seconds < 10) {
            timeElapsed.text = "$minutes:0$seconds"
        } else {
            timeElapsed.text = "$minutes:$seconds"
        }
    }

    private fun getDataFromUser(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val extras = intent.extras
            if (extras == null) {
                userName = null.toString()
                userFirebaseId = null.toString()
                userEmail = null.toString()
            } else {
                userEmail = extras.getString("PROFILE_PIC_EMAIL").toString()
                userFirebaseId = extras.getString("USER_FIREBASE_ID").toString()
                userName = extras.getString("USER_NAME").toString()
            }
        } else {
            userEmail = savedInstanceState.getSerializable("PROFILE_PIC_EMAIL").toString()
            userFirebaseId = savedInstanceState.getSerializable("USER_FIREBASE_ID").toString()
            userName = savedInstanceState.getSerializable("USER_NAME").toString()
        }
    }

    private fun setListeners() {
        playPauseButton.setOnClickListener {
            if (onOff) {
                onOff = false
                pauseSong()
            } else {
                onOff = true
                playSong()
            }
            currentSong()
        }
        skipForwardButton.setOnClickListener { skipForward() }
        skipBackwardButton.setOnClickListener { skipBackward() }
        backButton.setOnClickListener {
            val intent = Intent(this, BaseActivity::class.java)
            intent.putExtra("FRAGMENT_SELECTED", "Matches")
            startActivity(intent)
        }
    }

    private fun skipBackward() {
        mSpotifyAppRemote.playerApi.skipPrevious()
    }

    private fun skipForward() {
        mSpotifyAppRemote.playerApi.skipNext()
    }

    private fun playSong() {
        val string = mAuth.uid + userFirebaseId
        mSpotifyAppRemote.playerApi.resume()
        mDatabase.child(string).child("isPlaying").setValue(true)
    }

    private fun pauseSong() {
        val string = mAuth.uid + userFirebaseId
        mSpotifyAppRemote.playerApi.pause()
        mDatabase.child(string).child("isPlaying").setValue(false)
    }

    private fun setViews() {
        songImage = findViewById(R.id.songPicture)
        timeElapsed = findViewById(R.id.timeElapsed)
        songName = findViewById(R.id.songName)
        artistName = findViewById(R.id.artistName)
        playPauseButton = findViewById(R.id.play_pause_Button)
        skipForwardButton = findViewById(R.id.skip_forward_button)
        skipBackwardButton = findViewById(R.id.skip_back_button)
        userProfilePic = findViewById(R.id.profilePic)
        userNameText = findViewById(R.id.userName)
        backButton = findViewById(R.id.backButton)
    }

    private fun setImage(email: String) {
        val storageReference = FirebaseStorage.getInstance().reference
        val photoReference = storageReference.child("User").child(email).child("profilePic.png")
        val ONE_MEGABYTE = (1024 * 1024).toLong()
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            userProfilePic.setImageBitmap(
                Bitmap.createScaledBitmap(
                    getCroppedBitmap(bitmap),
                    90,
                    90,
                    true
                )
            )
        }.addOnFailureListener { }
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

    //Spotify App Remote Stuff
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
                    currentSong()
                    setFireBaseValueListenerOther()
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MainActivity", throwable.message, throwable)
                    // Something went wrong when attempting to connect! Handle errors here
                }
            })
    }

    private fun currentSong() {
        // Subscribe to PlayerState
        mSpotifyAppRemote.playerApi
            .subscribeToPlayerState()
            .setEventCallback { playerState: PlayerState ->
                val track = playerState.track
                val time = playerState.playbackPosition
                if (track != null) {
                    Log.d("MainActivity", track.name + " by " + track.artist.name)
                    setMusicPlayer(track, time)
                    spotifyTrack = track
                    updateMyRoom(track)
                }
            }
    }

    private fun setMusicPlayer(track: Track, time: Long) {
        Log.d("Locations updated", "location: $track")
        val imageUri = track.imageUri.toString()
        val uri = imageUri.substring(22, imageUri.length - 2)
        Picasso.get().isLoggingEnabled = true
        Picasso.get().load("https://i.scdn.co/image/$uri").resize(650, 600).into(songImage)
        songName.text = track.name
        artistName.text = track.artist.name
        val minutes = TimeUnit.MILLISECONDS.toMinutes(time)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60
        songDuration = track.duration
        timeElapsedCode = time
        if (seconds < 10) {
            timeElapsed.text = "$minutes:0$seconds"
        } else {
            timeElapsed.text = "$minutes:$seconds"
        }
    }

    private fun setFireBaseValueListenerOther() {
        val string = userFirebaseId + mAuth.uid
        val mSearchedLocationReference =
            FirebaseDatabase.getInstance().getReference("RoomData").child(string)
                .child("songPlayUri")
        mSearchedLocationReferenceListener =
            mSearchedLocationReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val `val` = dataSnapshot.value.toString()
                    Log.d("Locations updated", "location: $`val`")
                    mSpotifyAppRemote.playerApi.play(`val`)
                    // mSpotifyAppRemote.getPlayerApi().play(val);
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    private fun checkIfMyRoomExists() {
        val string = mAuth.uid + userFirebaseId
        val rootRef = FirebaseDatabase.getInstance().reference
        val roomRef = rootRef.child("RoomData").child(string)
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    //create new user
                    initializeMyRoom()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("TAG", databaseError.message) //Don't ignore errors!
            }
        }
        roomRef.addListenerForSingleValueEvent(eventListener)
    }

    private fun checkIfOtherRoomExists() {
        val string = userFirebaseId + mAuth.uid
        val rootRef = FirebaseDatabase.getInstance().reference
        val roomRef = rootRef.child("RoomData").child(string)
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    //create new user
                    initializeOtherRoom()
                } else {
                    val room = dataSnapshot.value as HashMap<String, Any>
                    val checker = room["isPlaying"] as Boolean
                    if (checker) {
                        mSpotifyAppRemote.playerApi.play(room["songPlayUri"].toString())
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("TAG", databaseError.message) //Don't ignore errors!
            }
        }
        roomRef.addListenerForSingleValueEvent(eventListener)
    }

    private fun updateMyRoom(track: Track) {
        val string = mAuth.uid + userFirebaseId
        mDatabase.child(string).child("songPlayUri").setValue(track.uri)
    }

    fun initializeMyRoom() {
        val hashRoom: MutableMap<String, Any> = HashMap()
        val songDur: Long = 0
        hashRoom["isPlaying"] = false
        hashRoom["songPlayUri"] = ""
        hashRoom["oneInRoom"] = true
        hashRoom["bothInRoom"] = false
        val string = mAuth.uid + userFirebaseId
        mDatabase.child(string).setValue(hashRoom)
    }

    fun initializeOtherRoom() {
        val hashRoom: MutableMap<String, Any> = HashMap()
        val songDur: Long = 0
        hashRoom["isPlaying"] = false
        hashRoom["songPlayUri"] = ""
        hashRoom["oneInRoom"] = true
        hashRoom["bothInRoom"] = false
        val string = userFirebaseId + mAuth.uid
        mDatabase.child(string).setValue(hashRoom)
    }

    override fun onStart() {
        super.onStart()
        checkIfOtherRoomExists()
        checkIfMyRoomExists()
        authenticateAppRemoteSpotify()
        // new Task().execute(songDuration);
    }

    override fun onStop() {
        super.onStop()
        SpotifyAppRemote.disconnect(mSpotifyAppRemote)
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }
}