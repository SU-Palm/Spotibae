package com.example.spotibae.Activities.Messaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spotibae.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MusicPlayer extends AppCompatActivity {
    ImageView songImage;
    TextView timeElapsed;
    TextView songName;
    TextView artistName;
    ImageView playPauseButton;
    ImageView skipForwardButton;
    ImageView skipBackwardButton;
    ImageView userProfilePic;
    TextView userNameText;
    boolean onOff = true;
    String userName;
    String userFirebaseId;
    String userEmail;
    long songDuration;
    long timeElapsedCode = 0;
    private static final String CLIENT_ID = "04fabd9b23d4470e8c29414d750c8d0f";
    private static final String REDIRECT_URI = "http://com.example.spotibae/callback";
    private SpotifyAppRemote mSpotifyAppRemote;
    public int i = 0;
    public boolean bothInRoom = true;
    public boolean oneInRoom = true;
    public Track spotifyTrack;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private ValueEventListener mSearchedLocationReferenceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("RoomData");
        getDataFromUser(savedInstanceState);
        setViews();
        setListeners();
        userNameText.setText(userName);
        setImage(userEmail);
    }

    private void setTime(long time) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60;
        if(seconds < 10) {
            timeElapsed.setText(String.valueOf(minutes) + ":" + "0" + String.valueOf(seconds));
        } else {
            timeElapsed.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
        }
    }

    private void getDataFromUser(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                userName = null;
                userFirebaseId = null;
                userEmail = null;
            } else {
                userEmail = extras.getString("PROFILE_PIC_EMAIL");
                userFirebaseId = extras.getString("USER_FIREBASE_ID");
                userName = extras.getString("USER_NAME");
            }
        } else {
            userEmail = (String) savedInstanceState.getSerializable("PROFILE_PIC_EMAIL");
            userFirebaseId = (String) savedInstanceState.getSerializable("USER_FIREBASE_ID");
            userName = (String) savedInstanceState.getSerializable("USER_NAME");
        }
    }

    private void setListeners() {
        playPauseButton.setOnClickListener( view -> {
            if(onOff) {
                /*
                Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.mp_play);
                playPauseButton.setImageBitmap(icon);
                //Picasso.get().load(icon).resize(50, 50).into(playPauseButton);
                */
                onOff = false;
                pauseSong();
            } else {
                /*
                Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.mp_pause);
                playPauseButton.setImageBitmap(icon);
                //Picasso.get().load(R.drawable.mp_pause).resize(50, 50).into(playPauseButton);
                */
                onOff = true;
                playSong();
            }
            getCurrentSong();
        });

        skipForwardButton.setOnClickListener( view -> {
            skipForward();
        });

        skipBackwardButton.setOnClickListener( view -> {
            skipBackward();
        });
    }

    private void skipBackward() {
        mSpotifyAppRemote.getPlayerApi().skipPrevious();
    }

    private void skipForward() {
        mSpotifyAppRemote.getPlayerApi().skipNext();
    }

    private void playSong() {
        String string = mAuth.getUid() + userFirebaseId;
        mSpotifyAppRemote.getPlayerApi().resume();
        mDatabase.child(string).child("isPlaying").setValue(true);
    }

    private void pauseSong() {
        String string = mAuth.getUid() + userFirebaseId;
        mSpotifyAppRemote.getPlayerApi().pause();
        mDatabase.child(string).child("isPlaying").setValue(false);
    }

    private void setViews() {
        songImage = findViewById(R.id.songPicture);
        timeElapsed = findViewById(R.id.timeElapsed);
        songName = findViewById(R.id.songName);
        artistName = findViewById(R.id.artistName);
        playPauseButton = findViewById(R.id.play_pause_Button);
        skipForwardButton = findViewById(R.id.skip_forward_button);
        skipBackwardButton = findViewById(R.id.skip_back_button);
        userProfilePic = findViewById(R.id.profilePic);
        userNameText = findViewById(R.id.userName);
    }

    public void setImage(String email) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference = storageReference.child("User").child(email).child("profilePic.png");

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                userProfilePic.setImageBitmap(Bitmap.createScaledBitmap(getCroppedBitmap(bitmap),  90,90, true));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    //Spotify App Remote Stuff
    public void authenticateAppRemoteSpotify() {
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {
                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");
                        // Now you can start interacting with App Remote
                        getCurrentSong();
                        setFireBaseValueListenerOther();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);
                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }

    public void getCurrentSong() {
        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    long time = playerState.playbackPosition;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                        setMusicPlayer(track, time);
                        spotifyTrack = track;
                        updateMyRoom(track);
                    }
                });
    }

    public void setMusicPlayer(Track track, long time) {
        Log.d("Locations updated", "location: " + track);
        String imageUri = track.imageUri.toString();
        String uri = imageUri.toString().substring(22, imageUri.toString().length() - 2);
        Picasso.get().setLoggingEnabled(true);
        Picasso.get().load("https://i.scdn.co/image/" + uri).resize(650, 600).into(songImage);
        songName.setText(track.name);
        artistName.setText(track.artist.name);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60;
        songDuration = track.duration;
        timeElapsedCode = time;
        if(seconds < 10) {
            timeElapsed.setText(String.valueOf(minutes) + ":" + "0" + String.valueOf(seconds));
        } else {
            timeElapsed.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
        }
    }

    private void setFireBaseValueListenerOther() {
        String string = userFirebaseId + mAuth.getUid();

        DatabaseReference mSearchedLocationReference = FirebaseDatabase.getInstance().getReference("RoomData").child(string).child("songPlayUri");
        mSearchedLocationReferenceListener = mSearchedLocationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String val = dataSnapshot.getValue().toString();
                Log.d("Locations updated", "location: " + val);
                mSpotifyAppRemote.getPlayerApi().play(val);
                // mSpotifyAppRemote.getPlayerApi().play(val);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setFireBaseValueListenerMine() {
        String string = mAuth.getUid() + userFirebaseId;

        DatabaseReference mSearchedLocationReference = FirebaseDatabase.getInstance().getReference("RoomData").child(string).child("songPlayUri");
        mSearchedLocationReferenceListener = mSearchedLocationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String val = dataSnapshot.getValue().toString();
                Log.d("Locations updated", "location: " + val);
                mSpotifyAppRemote.getPlayerApi().play(val);
                // mSpotifyAppRemote.getPlayerApi().play(val);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void checkIfMyRoomExists() {
        String string = mAuth.getUid() + userFirebaseId;
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference roomRef = rootRef.child("RoomData").child(string);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    //create new user
                    initializeMyRoom();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", databaseError.getMessage()); //Don't ignore errors!
            }
        };
        roomRef.addListenerForSingleValueEvent(eventListener);
    }

    public void checkIfOtherRoomExists() {
        String string = userFirebaseId + mAuth.getUid();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference roomRef = rootRef.child("RoomData").child(string);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    //create new user
                    initializeOtherRoom();
                } else {
                    HashMap<String, Object> room = (HashMap<String, Object>)dataSnapshot.getValue();
                    boolean checker = (boolean)room.get("isPlaying");
                    if(checker) {
                        mSpotifyAppRemote.getPlayerApi().play(room.get("songPlayUri").toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", databaseError.getMessage()); //Don't ignore errors!
            }
        };
        roomRef.addListenerForSingleValueEvent(eventListener);
    }

    public void updateMyRoom(Track track) {
        String string = mAuth.getUid() + userFirebaseId;
        mDatabase.child(string).child("songPlayUri").setValue(track.uri);
    }

    public void updateOtherRoom(Track track) {
        String string = userFirebaseId + mAuth.getUid();
        mDatabase.child(string).child("songPlayUri").setValue(track.uri);
    }

    public void initializeMyRoom() {
        Map<String, Object> hashRoom = new HashMap<String, Object>();
        long songDur = 0;
        hashRoom.put("isPlaying", false);
        hashRoom.put("songPlayUri", "");
        hashRoom.put("oneInRoom", true);
        hashRoom.put("bothInRoom", false);

        String string = mAuth.getUid() + userFirebaseId;
        mDatabase.child(string).setValue(hashRoom);
    }

    public void initializeOtherRoom() {
        Map<String, Object> hashRoom = new HashMap<String, Object>();
        long songDur = 0;
        hashRoom.put("isPlaying", false);
        hashRoom.put("songPlayUri", "");
        hashRoom.put("oneInRoom", true);
        hashRoom.put("bothInRoom", false);

        String string = userFirebaseId + mAuth.getUid();
        mDatabase.child(string).setValue(hashRoom);
    }

    private class Task extends AsyncTask<Long,Integer,List<String>> {

        @Override
        protected List<String> doInBackground(Long... time) {
            Log.d("Task", "doInBackground");
            Log.d("Task For Loop 2: ", String.valueOf(songDuration));

            for(long i = 0; i < 1000000; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(onOff) {
                    mSpotifyAppRemote.getPlayerApi().pause();
                } else {
                    mSpotifyAppRemote.getPlayerApi().resume();
                }
                if(isCancelled()) {
                    break;
                }
            }
            return null;
        }
    }

    public String createUniqueRoomId(String string) {
        Character tempArray[] = new Character[string.length()];
        for (int i = 0; i < string.length(); i++)
            tempArray[i] = string.charAt(i);

        Arrays.sort(tempArray, new Comparator<Character>() {
            @Override
            public int compare(Character c1, Character c2)
            {
                // Ignoring case
                return Character.compare(c1, c2);
            }
        });
        StringBuilder sb
                = new StringBuilder(tempArray.length);
        for (Character c : tempArray)
            sb.append(c.charValue());
        return sb.toString();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkIfMyRoomExists();
        authenticateAppRemoteSpotify();
        // new Task().execute(songDuration);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }
}