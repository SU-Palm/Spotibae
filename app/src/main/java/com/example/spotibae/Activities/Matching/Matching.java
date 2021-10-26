package com.example.spotibae.Activities.Matching;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.spotibae.Activities.Messaging.Messages;
import com.example.spotibae.Activities.Messaging.MessagesList;
import com.example.spotibae.Activities.Settings.Settings;
import com.example.spotibae.Activities.Welcome.Signup;
import com.example.spotibae.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Matching extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);
        this.setListeners();
        this.setNavBar();
    }

    private void setListeners() {

    }

    private void setNavBar() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.getMenu().findItem(R.id.navMatch).setChecked(true);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navMatch:
                        Intent intent1 = new Intent(Matching.this, Matching.class);
                        startActivity(intent1);
                        return true;
                    case R.id.navMessages:
                        Intent intent2 = new Intent(Matching.this, MessagesList.class);
                        startActivity(intent2);
                        return true;
                    case R.id.navSettings:
                        Intent intent3 = new Intent(Matching.this, Settings.class);
                        startActivity(intent3);
                        return true;
                    default:
                        // Do nothing
                }
                return true;
            }});
    }
}