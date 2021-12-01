package com.example.spotibae.Activities.Welcome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.spotibae.Activities.User.UserProfile;
import com.example.spotibae.Fragments.MatchingFrag;
import com.example.spotibae.Fragments.MessagesFrag;
import com.example.spotibae.Fragments.QRFrag;
import com.example.spotibae.Fragments.UserFrag;
import com.example.spotibae.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.HashMap;

public class BaseActivity extends AppCompatActivity {

    final FragmentManager fm = getSupportFragmentManager();
    ImageView profileImageButton;
    BottomNavigationView btm_nav;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    StorageReference profileRef;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    public String fragSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        btm_nav = findViewById(R.id.bottom_navigation);
        profileImageButton = findViewById(R.id.profilePic);
        System.out.println("FRAGMENT_SELECTED In User Profile onCreate() Before: " + fragSelected);
        fragSelected = isNotNullOrEmpty(getIntent().getStringExtra("FRAGMENT_SELECTED")) ? getIntent().getStringExtra("FRAGMENT_SELECTED") : "Dashboard";
        System.out.println("FRAGMENT_SELECTED In User Profile onCreate() After: " + fragSelected);
        if(fragSelected.equals("Dashboard")) {
            getFragment(new UserFrag());
            btm_nav.setSelectedItemId(R.id.navMatch);
            fragSelected = "Dashboard";
        } else if(fragSelected.equals("Matches")) {
            getFragment(new MessagesFrag());
            btm_nav.setSelectedItemId(R.id.navMessages);
            fragSelected = "Matches";
        } else {
            getFragment(new QRFrag());
            btm_nav.setSelectedItemId(R.id.navQR);
            fragSelected = "QR";
        }

        btm_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navMatch:
                        getFragment(new UserFrag());
                        fragSelected = "Dashboard";
                        return true;

                    case R.id.navMessages:
                        getFragment(new MessagesFrag());
                        fragSelected = "Matches";
                        return true;

                    case R.id.navQR:
                        getFragment(new QRFrag());
                        fragSelected = "QR";
                        return true;
                }
                return false;
            }
        });

        profileImageButton.setOnClickListener( view -> {
            Intent intent = new Intent(this, UserProfile.class);
            intent.putExtra("FRAGMENT_SELECTED", fragSelected);
            startActivity(intent);
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData");
        storageRef = storage.getReference();

        getEmailAndSetImage();
    }

    private static boolean isNotNullOrEmpty(String str){
        return (str != null && !str.isEmpty());
    }

    private void getEmailAndSetImage() {
        FirebaseUser user = mAuth.getCurrentUser();
        String uId = user.getUid();
        mDatabase.child(uId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    HashMap<String, Object> user = (HashMap<String, Object>) task.getResult().getValue();
                    String email = user.get("email").toString();
                    boolean firstLogin = Boolean.parseBoolean(user.get("firstLogin").toString());
                    if(firstLogin) {
                        Picasso.get().load(R.drawable.defaultprofile).centerCrop().resize(60,60).transform(new CircleTransform()).into(profileImageButton);
                    } else {
                        setImage(email);
                    }
                }
            }
        });
    }

    public void setImage(String email) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child("User").child(email).child("profilePic.png");

        photoReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // Pass it to Picasso to download, show in ImageView and caching
                Picasso.get().load(uri.toString()).centerCrop().resize(60,60).transform(new CircleTransform()).into(profileImageButton);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
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

    private void getFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("FRAGMENT_SELECTED In User Profile onStart() Before: " + fragSelected);
        fragSelected = isNotNullOrEmpty(getIntent().getStringExtra("FRAGMENT_SELECTED")) ? getIntent().getStringExtra("FRAGMENT_SELECTED") : "Dashboard";
        System.out.println("FRAGMENT_SELECTED In User Profile onStart() After: " + fragSelected);
        if(fragSelected.equals("Dashboard")) {
            getFragment(new UserFrag());
            btm_nav.setSelectedItemId(R.id.navMatch);
            fragSelected = "Dashboard";
        } else if(fragSelected.equals("Matches")) {
            getFragment(new MessagesFrag());
            btm_nav.setSelectedItemId(R.id.navMessages);
            fragSelected = "Matches";
        } else {
            getFragment(new QRFrag());
            btm_nav.setSelectedItemId(R.id.navQR);
            fragSelected = "QR";
        }
    }
}