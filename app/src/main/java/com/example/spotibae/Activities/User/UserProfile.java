package com.example.spotibae.Activities.User;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotibae.Activities.User.Settings.ChangeAgePreference;
import com.example.spotibae.Activities.User.Settings.ChangeBio;
import com.example.spotibae.Activities.User.Settings.ChangeDistance;
import com.example.spotibae.Activities.User.Settings.ChangeGender;
import com.example.spotibae.Activities.User.Settings.ChangeGenderMatchPreference;
import com.example.spotibae.Activities.User.Settings.ChangeLocation;
import com.example.spotibae.Activities.User.Settings.ChangeName;
import com.example.spotibae.Activities.User.Settings.ChangePhoneNumber;
import com.example.spotibae.Activities.Welcome.BaseActivity;
import com.example.spotibae.Activities.Welcome.WelcomeScreen;
import com.example.spotibae.Models.User;
import com.example.spotibae.R;
import com.example.spotibae.Services.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class UserProfile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    StorageReference profileRef;

    TextView doneButton;
    Button signOutButton;
    Button phoneNumberButton;
    Button distanceButton;
    Button genderPrefButton;
    Button bioButton;
    Button genderButton;
    Button nameButton;
    Button agePrefButton;
    Button verifySpotifyButton;
    Button locationButton;
    Button passwordResetButton;
    TextView profileName;
    TextView userEmailAddress;
    TextView userPhoneNumber;
    TextView userLocation;
    TextView userDistance;
    TextView userAgePref;
    TextView genderPrefText;
    TextView userBio;
    TextView userName;
    TextView userGender;

    // Uploading image and other stuff
    private Uri filepath;
    private final int PICK_IMAGE_REQUEST = -1;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ImageView profilePic;
    ImageView uploadPicButton;

    // Spotify
    private static final String CLIENT_ID = "04fabd9b23d4470e8c29414d750c8d0f";
    private static final String REDIRECT_URI = "http://com.example.spotibae/callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setButtons();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData");
        setTextViews();
        initData();

        profilePic = findViewById(R.id.profilePic);
        uploadPicButton = findViewById(R.id.uploadPicButton);

        storageRef = storage.getReference();
        //profileRef = storageRef.child("User"); //.child("Joe").child("profilePic.jpg");

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        System.out.println("Result Code: " + result.getResultCode() + " ");
                        if(result.getResultCode() == PICK_IMAGE_REQUEST) {
                            filepath = result.getData().getData();
                            System.out.println("Printing Image Text");
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                                profilePic.setImageBitmap(Bitmap.createScaledBitmap(getCroppedBitmap(bitmap),  350 ,400, true));

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();
                                profileRef = storageRef.child("User").child(userEmailAddress.getText().toString()).child("profilePic.png");
                                UploadTask uploadTask = profileRef.putBytes(data);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                        // ...
                                    }
                                });
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        uploadPicButton.setOnClickListener( view -> {
            chooseImage();
        });

        verifySpotifyButton = findViewById(R.id.connectSpotify);

        verifySpotifyButton.setOnClickListener(view -> {
            authenticateSpotify();
        });

        locationButton = findViewById(R.id.locationButton);
        locationButton.setOnClickListener( view -> {
            Intent intent = new Intent(this, ChangeLocation.class);
            startActivity(intent);
        });

        passwordResetButton = findViewById(R.id.resetPassword);
        passwordResetButton.setOnClickListener( view -> {
            passwordReset();
        });

    }

    public void setImage(String email) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child("User").child(email).child("profilePic.png");

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profilePic.setImageBitmap(Bitmap.createScaledBitmap(getCroppedBitmap(bitmap),  350 ,400, true));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        activityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    public void setButtons() {
        doneButton = findViewById(R.id.done);
        doneButton.setOnClickListener( view -> {
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        });
        signOutButton = findViewById(R.id.signOut);
        signOutButton.setOnClickListener(v -> signOut());
        phoneNumberButton = findViewById(R.id.phoneNumberButton);
        phoneNumberButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChangePhoneNumber.class);
            startActivity(intent);
        });
        distanceButton = findViewById(R.id.distanceButton);
        distanceButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChangeDistance.class);
            startActivity(intent);
        });
        genderPrefButton = findViewById(R.id.preferenceButton);
        genderPrefButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChangeGenderMatchPreference.class);
            startActivity(intent);
        });
        bioButton = findViewById(R.id.bioButton);
        bioButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChangeBio.class);
            startActivity(intent);
        });
        genderButton = findViewById(R.id.genderButton);
        genderButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChangeGender.class);
            startActivity(intent);
        });
        nameButton = findViewById(R.id.nameButton);
        nameButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChangeName.class);
            startActivity(intent);
        });
        agePrefButton = findViewById(R.id.ageButton);
        agePrefButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChangeAgePreference.class);
            startActivity(intent);
        });
    }

    public void setTextViews() {
        profileName = findViewById(R.id.profileName);
        userEmailAddress = findViewById(R.id.userEmailAddress);
        userPhoneNumber = findViewById(R.id.userPhoneNumber);
        userLocation = findViewById(R.id.userLocation);
        userDistance = findViewById(R.id.userDistance);
        userAgePref = findViewById(R.id.userAgePref);
        genderPrefText = findViewById(R.id.genderPref);
        userBio = findViewById(R.id.userBio);
        userName = findViewById(R.id.userName);
        userGender = findViewById(R.id.userGender);
    }


    public void initData() {
        FirebaseUser user = mAuth.getCurrentUser();
        String uId = user.getUid();

        mDatabase.child(uId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase User", String.valueOf(task.getResult().getValue()));
                    HashMap<String, Object> user = (HashMap<String, Object>) task.getResult().getValue();
                    String firstName = user.get("firstName").toString();
                    String lastName = user.get("lastName").toString();
                    long age = (long)user.get("age");
                    String email = user.get("email").toString();
                    String fullName = user.get("fullName").toString();
                    String bio = user.get("bio").toString();
                    String gender = user.get("gender").toString();
                    String phoneNumber = user.get("phoneNumber").toString();
                    long distance = (long)user.get("distance");
                    long lowestAgePref = (long)user.get("lowestAgePref");
                    long highestAgePref = (long)user.get("highestAgePref");
                    String genderPref = user.get("genderPref").toString();
                    boolean spotifyVerified = (boolean) user.get("spotifyVerified");
                    String location = user.get("location").toString();
                    profileName.setText(firstName);
                    userEmailAddress.setText(email);
                    userPhoneNumber.setText(phoneNumber);
                    userLocation.setText(location);
                    userDistance.setText(String.valueOf(distance));
                    userAgePref.setText(String.valueOf(lowestAgePref) + " - " + String.valueOf(highestAgePref));
                    genderPrefText.setText(genderPref);
                    userBio.setText(bio);
                    userName.setText(fullName);
                    userGender.setText(gender);
                    setImage(email);
                }
            }
        });
    }

    public void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(this, WelcomeScreen.class);
        this.startActivity(intent);
    }

    public void passwordReset() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(userEmailAddress.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("MainActivity", "Email sent.");
                        }
                    }
                });
    }

    public void authenticateSpotify() {
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
                        FirebaseUser user = mAuth.getCurrentUser();
                        String uid = user.getUid();
                        mDatabase.child(uid).child("spotifyVerified").setValue(true);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });

    }
}