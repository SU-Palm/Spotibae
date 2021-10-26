package com.example.spotibae.Activities.Messaging;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.spotibae.Activities.Matching.Matching;
import com.example.spotibae.Activities.Settings.Settings;
import com.example.spotibae.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MessagesList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        this.setNavBar();
    }

    private void setNavBar() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.getMenu().findItem(R.id.navMessages).setChecked(true);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navMatch:
                        Intent intent1 = new Intent(MessagesList.this, Matching.class);
                        startActivity(intent1);
                        return true;
                    case R.id.navMessages:
                        Intent intent2 = new Intent(MessagesList.this, MessagesList.class);
                        startActivity(intent2);
                        return true;
                    case R.id.navSettings:
                        Intent intent3 = new Intent(MessagesList.this, Settings.class);
                        startActivity(intent3);
                        return true;
                    default:
                        // Do nothing
                }
                return true;
            }});
    }
}