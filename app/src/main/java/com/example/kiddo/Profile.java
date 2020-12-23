package com.example.kiddo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {
    private String myFullname, myRole = "";
    private TextView txtFullname, txtUsername, txtRole, txtPK, txtGender, txtDOB, txtStars;

    // Key for current fullname
    private final String FULLNAME_KEY = "fullname";

    // Shared preferences object
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    // Name of shared preferences file
    private String sharedPrefFile =
            "com.example.android.hellosharedprefs";

    RequestQueue requestQueue;

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

        requestQueue = Volley.newRequestQueue(this);

        LinearLayout linearLayout, linearLayout2, linearLayout3, linearLayout4;

        linearLayout = findViewById(R.id.linearLayout);
        linearLayout2 = findViewById(R.id.linearLayout2);
        linearLayout3 = findViewById(R.id.linearLayout3);
        linearLayout4 = findViewById(R.id.linearLayout4);

        txtFullname = (TextView) findViewById(R.id.profile_fullname);
        txtUsername = (TextView) findViewById(R.id.profile_username);
        txtRole = (TextView) findViewById(R.id.profile_role);
        txtPK = (TextView) findViewById(R.id.profile_pk);
        txtGender = (TextView) findViewById(R.id.profile_sex);
        txtDOB = (TextView) findViewById(R.id.profile_dob);
        txtStars = (TextView) findViewById(R.id.profile_stars);

        txtUsername.setText(myFullname);
        txtRole.setText(myRole);

        if (myRole.contains("Child")) {
            getChildProfile();
        }

        if (myRole.contains("Parent")) {
            getParentProfile();
            linearLayout.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.GONE);
            linearLayout3.setVisibility(View.GONE);
            linearLayout4.setVisibility(View.GONE);
        }

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

    private void getChildProfile() {
        String url = Configuration.URL_GET_CHILD_PROFILE;
        JsonObjectRequest jsonObjectRequest__ = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String username = jsonObject.optString("username");
                        String parent_username = jsonObject.optString("parent_username");
                        String full_name = jsonObject.optString("full_name");
                        String sex_ = jsonObject.optString("sex");
                        String dob_ = jsonObject.optString("dob");
                        String stars_collected = jsonObject.optString("stars_collected");
                        if (username.equals(myFullname)) {
                            txtFullname.setText(full_name);
                            txtPK.setText(parent_username);
                            txtStars.setText(stars_collected);
                            txtGender.setText(sex_);
                            txtDOB.setText(dob_);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest__);

    }

    private void getParentProfile() {
        String url = Configuration.URL_GET_PARENT_PROFILE;
        JsonObjectRequest jsonObjectRequest_ = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String username = jsonObject.optString("username");
                        String full_name = jsonObject.optString("full_name");
                        if (username.equals(myFullname)) {
                            txtFullname.setText(full_name);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest_);
    }

}