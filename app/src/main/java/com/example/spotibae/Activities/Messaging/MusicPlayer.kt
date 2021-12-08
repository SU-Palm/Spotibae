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
    var songImage: ImageView? = null
    var timeElapsed: TextView? = null
    var songName: TextView? = null
    var artistName: TextView? = null
    var playPauseButton: ImageView? = null
    var skipForwardButton: ImageView? = null
    var skipBackwardButton: ImageView? = null
    var userProfilePic: ImageView? = null
    var userNameText: TextView? = null
    var onOff = true
    var userName: String? = null
    var userFirebaseId: String? = null
    var userEmail: String? = null
    var songDuration: Long = 0
    var timeElapsedCode: Long = 0
    private var mSpotifyAppRemote: SpotifyAppRemote? = null
    var i = 0
    var bothInRoom = true
    var oneInRoom = true
    var spotifyTrack: Track? = null
    var backButton: ImageView? = null
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null
    var storage = FirebaseStorage.getInstance()
    private var mSearchedLocationReferenceListener: ValueEventListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference("RoomData")
        getDataFromUser(savedInstanceState)
        setViews()
        setListeners()
        userNameText!!.text = userName
        setImage(userEmail)
    }

    private fun setTime(time: Long) {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(time)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60
        if (seconds < 10) {
            timeElapsed!!.text = "$minutes:0$seconds"
        } else {
            timeElapsed!!.text = "$minutes:$seconds"
        }
    }

    private fun getDataFromUser(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val extras = intent.extras
            if (extras == null) {
                userName = null
                userFirebaseId = null
                userEmail = null
            } else {
                userEmail = extras.getString("PROFILE_PIC_EMAIL")
                userFirebaseId = extras.getString("USER_FIREBASE_ID")
                userName = extras.getString("USER_NAME")
            }
        } else {
            userEmail = savedInstanceState.getSerializable("PROFILE_PIC_EMAIL") as String?
            userFirebaseId = savedInstanceState.getSerializable("USER_FIREBASE_ID") as String?
            userName = savedInstanceState.getSerializable("USER_NAME") as String?
        }
    }

    private fun setListeners() {
        playPauseButton!!.setOnClickListener { view: View? ->
            if (onOff) {
                /*
                Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.mp_play);
                playPauseButton.setImageBitmap(icon);
                //Picasso.get().load(icon).resize(50, 50).into(playPauseButton);
                */
                onOff = false
                pauseSong()
            } else {
                /*
                Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.mp_pause);
                playPauseButton.setImageBitmap(icon);
                //Picasso.get().load(R.drawable.mp_pause).resize(50, 50).into(playPauseButton);
                */
                onOff = true
                playSong()
            }
            currentSong
        }
        skipForwardButton!!.setOnClickListener { view: View? -> skipForward() }
        skipBackwardButton!!.setOnClickListener { view: View? -> skipBackward() }
        backButton!!.setOnClickListener { view: View? ->
            val intent = Intent(this, BaseActivity::class.java)
            intent.putExtra("FRAGMENT_SELECTED", "Matches")
            startActivity(intent)
        }
    }

    private fun skipBackward() {
        mSpotifyAppRemote!!.playerApi.skipPrevious()
    }

    private fun skipForward() {
        mSpotifyAppRemote!!.playerApi.skipNext()
    }

    private fun playSong() {
        val string = mAuth!!.uid + userFirebaseId
        mSpotifyAppRemote!!.playerApi.resume()
        mDatabase!!.child(string).child("isPlaying").setValue(true)
    }

    private fun pauseSong() {
        val string = mAuth!!.uid + userFirebaseId
        mSpotifyAppRemote!!.playerApi.pause()
        mDatabase!!.child(string).child("isPlaying").setValue(false)
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

    fun setImage(email: String?) {
        val storageReference = FirebaseStorage.getInstance().reference
        val photoReference = storageReference.child("User").child(email!!).child("profilePic.png")
        val ONE_MEGABYTE = (1024 * 1024).toLong()
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            userProfilePic!!.setImageBitmap(
                Bitmap.createScaledBitmap(
                    getCroppedBitmap(bitmap),
                    90,
                    90,
                    true
                )
            )
        }.addOnFailureListener { }
    }

    fun getCroppedBitmap(bitmap: Bitmap): Bitmap {
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
    fun authenticateAppRemoteSpotify() {
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
                    currentSong
                    setFireBaseValueListenerOther()
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MainActivity", throwable.message, throwable)
                    // Something went wrong when attempting to connect! Handle errors here
                }
            })
    }

    // Subscribe to PlayerState
    val currentSong: Unit
        get() {
            // Subscribe to PlayerState
            mSpotifyAppRemote!!.playerApi
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

    fun setMusicPlayer(track: Track, time: Long) {
        Log.d("Locations updated", "location: $track")
        val imageUri = track.imageUri.toString()
        val uri = imageUri.substring(22, imageUri.length - 2)
        Picasso.get().isLoggingEnabled = true
        Picasso.get().load("https://i.scdn.co/image/$uri").resize(650, 600).into(songImage)
        songName!!.text = track.name
        artistName!!.text = track.artist.name
        val minutes = TimeUnit.MILLISECONDS.toMinutes(time)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60
        songDuration = track.duration
        timeElapsedCode = time
        if (seconds < 10) {
            timeElapsed!!.text = "$minutes:0$seconds"
        } else {
            timeElapsed!!.text = "$minutes:$seconds"
        }
    }

    private fun setFireBaseValueListenerOther() {
        val string = userFirebaseId + mAuth!!.uid
        val mSearchedLocationReference =
            FirebaseDatabase.getInstance().getReference("RoomData").child(string)
                .child("songPlayUri")
        mSearchedLocationReferenceListener =
            mSearchedLocationReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val `val` = dataSnapshot.value.toString()
                    Log.d("Locations updated", "location: $`val`")
                    mSpotifyAppRemote!!.playerApi.play(`val`)
                    // mSpotifyAppRemote.getPlayerApi().play(val);
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    private fun setFireBaseValueListenerMine() {
        val string = mAuth!!.uid + userFirebaseId
        val mSearchedLocationReference =
            FirebaseDatabase.getInstance().getReference("RoomData").child(string)
                .child("songPlayUri")
        mSearchedLocationReferenceListener =
            mSearchedLocationReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val `val` = dataSnapshot.value.toString()
                    Log.d("Locations updated", "location: $`val`")
                    mSpotifyAppRemote!!.playerApi.play(`val`)
                    // mSpotifyAppRemote.getPlayerApi().play(val);
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun checkIfMyRoomExists() {
        val string = mAuth!!.uid + userFirebaseId
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

    fun checkIfOtherRoomExists() {
        val string = userFirebaseId + mAuth!!.uid
        val rootRef = FirebaseDatabase.getInstance().reference
        val roomRef = rootRef.child("RoomData").child(string)
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    //create new user
                    initializeOtherRoom()
                } else {
                    val room = dataSnapshot.value as HashMap<String, Any>?
                    val checker = room!!["isPlaying"] as Boolean
                    if (checker) {
                        mSpotifyAppRemote!!.playerApi.play(room["songPlayUri"].toString())
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("TAG", databaseError.message) //Don't ignore errors!
            }
        }
        roomRef.addListenerForSingleValueEvent(eventListener)
    }

    fun updateMyRoom(track: Track) {
        val string = mAuth!!.uid + userFirebaseId
        mDatabase!!.child(string).child("songPlayUri").setValue(track.uri)
    }

    fun updateOtherRoom(track: Track) {
        val string = userFirebaseId + mAuth!!.uid
        mDatabase!!.child(string).child("songPlayUri").setValue(track.uri)
    }

    fun initializeMyRoom() {
        val hashRoom: MutableMap<String, Any> = HashMap()
        val songDur: Long = 0
        hashRoom["isPlaying"] = false
        hashRoom["songPlayUri"] = ""
        hashRoom["oneInRoom"] = true
        hashRoom["bothInRoom"] = false
        val string = mAuth!!.uid + userFirebaseId
        mDatabase!!.child(string).setValue(hashRoom)
    }

    fun initializeOtherRoom() {
        val hashRoom: MutableMap<String, Any> = HashMap()
        val songDur: Long = 0
        hashRoom["isPlaying"] = false
        hashRoom["songPlayUri"] = ""
        hashRoom["oneInRoom"] = true
        hashRoom["bothInRoom"] = false
        val string = userFirebaseId + mAuth!!.uid
        mDatabase!!.child(string).setValue(hashRoom)
    }

    fun createUniqueRoomId(string: String): String {
        val tempArray = arrayOfNulls<Char>(string.length)
        for (i in 0 until string.length) tempArray[i] = string[i]
        Arrays.sort(tempArray) { c1, c2 -> // Ignoring case
            Character.compare(c1!!, c2!!)
        }
        val sb = StringBuilder(tempArray.size)
        for (c in tempArray) sb.append(c!!.toChar())
        return sb.toString()
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

    companion object {
        private const val CLIENT_ID = BuildConfig.CLIENT_ID
        private const val REDIRECT_URI = "http://com.example.spotibae/callback"
    }
}