package com.example.kiddo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends AppCompatActivity {
    private String myFullname, myRole = "";

    // Key for current fullname
    private final String FULLNAME_KEY = "fullname";

    // Shared preferences object
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    // Name of shared preferences file
    private String sharedPrefFile =
            "com.example.android.hellosharedprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String _myFullname =  getIntent().getStringExtra("MY_FULLNAME");
        String _myRole =  getIntent().getStringExtra("MY_ROLE");
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.putString(FULLNAME_KEY, _myFullname);
        mEditor.putString("role", _myRole);
        mEditor.commit();

        myFullname = mPreferences.getString(FULLNAME_KEY, _myFullname);
        myRole = mPreferences.getString("role", _myRole);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        return  true;
                    case R.id.home:
                        Intent intent = new Intent(Profile.this, MainActivity.class);
                        intent.putExtra("MY_FULLNAME", myFullname);
                        intent.putExtra("MY_ROLE", myRole);
                        startActivity(intent);
//                        startActivity(new Intent(Profile.this, MainActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.gift:
                        Intent intent_ = new Intent(Profile.this, Gift.class);
                        intent_.putExtra("MY_FULLNAME", myFullname);
                        intent_.putExtra("MY_ROLE", myRole);
                        startActivity(intent_);
//                        startActivity(new Intent(Profile.this, Gift.class));
                        overridePendingTransition(0,0);
                        return  true;
                }
                return false;
            }
        });
    }
}