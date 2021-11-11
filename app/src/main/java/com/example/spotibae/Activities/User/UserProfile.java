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
import com.example.spotibae.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    // Testing
    TextView userSpotifyData;
    Button getToken;
    Button getCode;

    // Uploading image and other stuff
    private Uri filepath;
    private final int PICK_IMAGE_REQUEST = -1;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ImageView profilePic;
    ImageView uploadPicButton;

    // Keys for persistent storage
    private final String EMAIL_KEY = "email";
    private final String PHONE_NUM_KEY = "phone";
    private final String LOCATION_KEY = "location";
    private final String DISTANCE_KEY = "distance";
    private final String AGE_PREF_KEY = "age";
    private final String SHOW_ME_KEY = "showMe";
    private final String BIO_KEY = "bio";
    private final String NAME_KEY = "name";
    private final String GENDER_KEY = "gender";

    // Spotify Remote App
    private static final String CLIENT_ID = "04fabd9b23d4470e8c29414d750c8d0f";
    private static final String REDIRECT_URI = "http://com.example.spotibae/callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    // Spotify Auth
    public static final int AUTH_TOKEN_REQUEST_CODE = 0x10;
    public static final int AUTH_CODE_REQUEST_CODE = 0x11;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken;
    private String mAccessCode;
    private Call mCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initializing
        setViews();
        setListeners();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData");
        storageRef = storage.getReference();

        if(savedInstanceState != null) {
            profileName.setText(savedInstanceState.getString(NAME_KEY));
            userEmailAddress.setText(savedInstanceState.getString(EMAIL_KEY));
            userPhoneNumber.setText(savedInstanceState.getString(PHONE_NUM_KEY));
            userLocation.setText(savedInstanceState.getString(LOCATION_KEY));
            userDistance.setText(savedInstanceState.getString(DISTANCE_KEY));
            userAgePref.setText(savedInstanceState.getString(AGE_PREF_KEY));
            genderPrefText.setText(savedInstanceState.getString(SHOW_ME_KEY));
            userBio.setText(savedInstanceState.getString(BIO_KEY));
            userName.setText(savedInstanceState.getString(NAME_KEY));
            userGender.setText(savedInstanceState.getString(GENDER_KEY));
        } else {
            initData();
        }

        // Callback for picking image in gallery
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        System.out.println("Result Code: " + result.getResultCode() + " ");
                        if(result.getResultCode() == PICK_IMAGE_REQUEST) {
                            filepath = result.getData().getData();
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                                profilePic.setImageBitmap(Bitmap.createScaledBitmap(getCroppedBitmap(bitmap),  350 ,400, true));
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();
                                profileRef = storageRef.child("User").child(userEmailAddress.getText().toString()).child("profilePic.png");
                                UploadTask uploadTask = profileRef.putBytes(data);

                                // Saving image to external storage
                                /*
                                String path = Environment.getExternalStorageDirectory().toString();
                                Bitmap bitmapSave = Bitmap.createScaledBitmap(getCroppedBitmap(bitmap),  350 ,400, true);
                                OutputStream fOut = null;
                                Integer counter = 0;
                                File file = new File(path, "profilePhoto"+counter+".jpg");
                                fOut = new FileOutputStream(file);
                                bitmapSave.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                                fOut.flush();
                                fOut.close();
                                MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
                                 */
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
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(EMAIL_KEY, userEmailAddress.getText().toString());
        outState.putString(PHONE_NUM_KEY, userPhoneNumber.getText().toString());
        outState.putString(LOCATION_KEY, userLocation.getText().toString());
        outState.putString(DISTANCE_KEY, userDistance.getText().toString());
        outState.putString(AGE_PREF_KEY, userAgePref.getText().toString());
        outState.putString(SHOW_ME_KEY, genderPrefText.getText().toString());
        outState.putString(BIO_KEY, userBio.getText().toString());
        outState.putString(NAME_KEY, userName.getText().toString());
        outState.putString(GENDER_KEY, userGender.getText().toString());
        super.onSaveInstanceState(outState);
    }

    public void setImage(String email) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference = storageReference.child("User").child(email).child("profilePic.png");

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

    public void setListeners() {
        doneButton.setOnClickListener( view -> {
            Intent intent = new Intent(this, BaseActivity.class);
            startActivity(intent);
        });
        signOutButton.setOnClickListener(v -> {
            signOut();
        });
        phoneNumberButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChangePhoneNumber.class);
            startActivity(intent);
        });
        distanceButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChangeDistance.class);
            startActivity(intent);
        });
        genderPrefButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChangeGenderMatchPreference.class);
            startActivity(intent);
        });
        bioButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChangeBio.class);
            startActivity(intent);
        });
        genderButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChangeGender.class);
            startActivity(intent);
        });
        nameButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChangeName.class);
            startActivity(intent);
        });
        agePrefButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChangeAgePreference.class);
            startActivity(intent);
        });
        uploadPicButton.setOnClickListener( view -> {
            chooseImage();
        });
        verifySpotifyButton.setOnClickListener(view -> {
            authenticateAppRemoteSpotify();
        });
        locationButton.setOnClickListener( view -> {
            Intent intent = new Intent(this, ChangeLocation.class);
            startActivity(intent);
        });
        passwordResetButton.setOnClickListener( view -> {
            passwordReset();
        });


        // Testing Spotify Auth
        getToken.setOnClickListener( view -> {
            onRequestTokenClicked();
        });
        getCode.setOnClickListener( view -> {
            onRequestCodeClicked();
        });


    }

    public void setViews() {
        // TextViews
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

        // Buttons
        nameButton = findViewById(R.id.nameButton);
        agePrefButton = findViewById(R.id.ageButton);
        genderButton = findViewById(R.id.genderButton);
        bioButton = findViewById(R.id.bioButton);
        genderPrefButton = findViewById(R.id.preferenceButton);
        distanceButton = findViewById(R.id.distanceButton);
        phoneNumberButton = findViewById(R.id.phoneNumberButton);
        signOutButton = findViewById(R.id.signOut);
        doneButton = findViewById(R.id.done);
        uploadPicButton = findViewById(R.id.uploadPicButton);
        verifySpotifyButton = findViewById(R.id.connectSpotify);
        locationButton = findViewById(R.id.locationButton);
        passwordResetButton = findViewById(R.id.resetPassword);

        //ImageView
        profilePic = findViewById(R.id.profilePic);

        // Spotify Testing
        userSpotifyData = findViewById(R.id.response_text_view);
        getToken = findViewById(R.id.getToken);
        getCode = findViewById(R.id.getCode);
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
                        FirebaseUser user = mAuth.getCurrentUser();
                        String uid = user.getUid();
                        mDatabase.child(uid).child("spotifyVerified").setValue(true);
                        authenticateAuthSpotify();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }


    // Spotify Stuff
    public void authenticateAuthSpotify() {
        //onGetUserTopTracks();
        onGetUserTopArtists();
    }

    public void onRequestCodeClicked() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.CODE);
        AuthorizationClient.openLoginActivity(this, AUTH_CODE_REQUEST_CODE, request);
    }

    public void onRequestTokenClicked() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    private void setResponse(final String text) {
        runOnUiThread(() -> {
            final TextView responseView = findViewById(R.id.response_text_view);
            responseView.setText(text);
        });
    }

    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, REDIRECT_URI)
                .setShowDialog(false)
                .setScopes(new String[]{"user-read-email", "user-top-read"})
                .setCampaign("your-campaign-token")
                .build();
    }

    private void updateTokenView() {
        final TextView tokenView = findViewById(R.id.token_text_view);
        tokenView.setText(getString(R.string.token, mAccessToken));
    }

    private void updateCodeView() {
        final TextView codeView = findViewById(R.id.code_text_view);
        codeView.setText(getString(R.string.code, mAccessCode));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);
        if (response.getError() != null && !response.getError().isEmpty()) {
            Log.d("Spotify Auth", "Failed");
        }
        if (requestCode == AUTH_TOKEN_REQUEST_CODE) {
            mAccessToken = response.getAccessToken();
            updateTokenView();
        } else if (requestCode == AUTH_CODE_REQUEST_CODE) {
            mAccessCode = response.getCode();
            updateCodeView();
        }
    }

    public void onGetUserTopTracks() {
        if (mAccessToken == null) {
            Log.d("Spotify Auth", "Failed at onGetUserProfileClicked()");
            return;
        }

        final Request requestTopTracks = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/tracks?time_range=medium_term&limit=2&offset=1")
                .addHeader("Authorization","Bearer " + mAccessToken)
                .addHeader("Content-Type", "application/json")
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(requestTopTracks);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                setResponse("Failed to fetch data: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    setResponse(jsonObject.toString(3));
                } catch (JSONException e) {
                    setResponse("Failed to parse data: " + e);
                }
            }
        });
    }

    public void onGetUserTopArtists() {
        if (mAccessToken == null) {
            Log.d("Spotify Auth", "Failed at onGetUserProfileClicked()");
            return;
        }

        final Request requestTopTracks = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/artists?time_range=medium_term&limit=2&offset=1")
                .addHeader("Authorization","Bearer " + mAccessToken)
                .addHeader("Content-Type", "application/json")
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(requestTopTracks);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                setResponse("Failed to fetch data: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    setResponse(jsonObject.toString(3));
                } catch (JSONException e) {
                    setResponse("Failed to parse data: " + e);
                }
            }
        });
    }

    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

}