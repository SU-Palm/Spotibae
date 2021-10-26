package com.example.spotibae.Activities.Welcome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.example.spotibae.Activities.User.UserProfile;
import com.example.spotibae.Fragments.MatchingFrag;
import com.example.spotibae.Fragments.MessagesFrag;
import com.example.spotibae.Fragments.QRFrag;
import com.example.spotibae.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {

    final FragmentManager fm = getSupportFragmentManager();
    ImageView profileImageButton;
    BottomNavigationView btm_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        btm_nav = findViewById(R.id.bottom_navigation);
        profileImageButton = findViewById(R.id.profilePic);

        getFragment(new MatchingFrag());

        btm_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navMatch:
                        getFragment(new MatchingFrag());
                        return true;

                    case R.id.navMessages:
                        getFragment(new MessagesFrag());
                        return true;

                    case R.id.navQR:
                        getFragment(new QRFrag());
                        return true;
                }
                return false;
            }
        });

        profileImageButton.setOnClickListener( view -> {
            Intent intent = new Intent(this, UserProfile.class);
            startActivity(intent);
        });
    }

    private void getFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }
}