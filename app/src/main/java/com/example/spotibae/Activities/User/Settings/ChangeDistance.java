package com.example.spotibae.Activities.User.Settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotibae.Activities.User.UserProfile;
import com.example.spotibae.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeDistance extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Button doneButton;
    ImageView backButton;
    TextView distance;

    SeekBar seekBar;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_distance);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("UserData");
        String uid = mAuth.getUid();

        doneButton = findViewById(R.id.changeDistance);
        distance = findViewById(R.id.totalDistance);

        String userDistance = getIntent().getStringExtra("CURRENT_DISTANCE");
        distance.setText(userDistance);

        doneButton.setOnClickListener( view -> {
            String checker = distance.getText().toString();
            if(checker.isEmpty()) {
                Intent intent = new Intent(this, UserProfile.class);
                String fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString();
                intent.putExtra("FRAGMENT_SELECTED", fragSelected);
                startActivity(intent);
            } else {
                long distanceNum = Long.parseLong(distance.getText().toString());
                changeDistance(distanceNum, uid);

                Intent intent = new Intent(this, UserProfile.class);
                String fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString();
                intent.putExtra("FRAGMENT_SELECTED", fragSelected);
                startActivity(intent);
            }
        });

        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener( view -> {
            Intent intent = new Intent(this, UserProfile.class);
            String fragSelected = getIntent().getStringExtra("FRAGMENT_SELECTED").toString();
            intent.putExtra("FRAGMENT_SELECTED", fragSelected);
            startActivity(intent);
        });

        seekBar = findViewById(R.id.changeDistanceSeekBar);

        seekBar.setMax(200);
        seekBar.setMin(0);
        seekBar.setProgress(Integer.parseInt(userDistance));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                distance.setText(String.valueOf(progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(ChangeDistance.this, "Seek bar progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void changeDistance(long distanceNum, String uid) {
        mDatabase.child(uid).child("distance").setValue(distanceNum);
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
}