package com.example.kiddo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String myFullname = "";
    private TextView txtFullname;

    // Key for current fullname
    private final String FULLNAME_KEY = "fullname";

    // Shared preferences object
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    // Name of shared preferences file
    private String sharedPrefFile =
            "com.example.android.hellosharedprefs";

    RecyclerView recyclerView;
    MyAdapter myAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String _myFullname =  getIntent().getStringExtra("MY_FULLNAME");
        txtFullname = (TextView) findViewById(R.id.myFullname);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.putString(FULLNAME_KEY, _myFullname);
        mEditor.commit();

        // Restore preferences
        myFullname = mPreferences.getString(FULLNAME_KEY, _myFullname);
        txtFullname.setText(String.format("%s", myFullname));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.home:
                        return  true;
                    case R.id.gift:
                        startActivity(new Intent(getApplicationContext(), Gift.class));
                        overridePendingTransition(0,0);
                        return  true;
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new MyAdapter(this, getMyList());
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        reset(txtFullname);
//        Intent intent = new Intent(getApplicationContext(), Login.class);
//        startActivity(intent);
//        finish();
    }

    private ArrayList<Model> getMyList() {
        final ArrayList<Model> models = new ArrayList<>();

//        String url = Configuration.URL_GET_ALL_MAIN_ACTIVITIES;
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("result");
//                    for(int i=0; i<jsonArray.length();i++) {
//                        JSONObject jsonObject =jsonArray.getJSONObject(i);
////                        if (jsonObject.optString("created_by") == myFullname || jsonObject.optString("created_for") == myFullname) {
//                            String title = jsonObject.optString("title");
//                            String subtitle = jsonObject.optString("subtitle");
//                            Model m = new Model();
//                            m.setTitle(title);
//                            m.setSubtitle(subtitle);
//                            models.add(m);
////                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });

//        requestQueue.add(jsonObjectRequest);


        Model m = new Model();
        m.setTitle("Test dulu pertmaa ya");
        m.setSubtitle("ini juga harus test panjang panjang bair ngena giuuuu");
        models.add(m);

        m = new Model();
        m.setTitle("test kedua");
        m.setSubtitle("ini jugaa kedua");
        models.add(m);

        return models;
    }

    /**
     * Callback for activity pause.  Shared preferences are saved here.
     */
    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(FULLNAME_KEY, myFullname);
        preferencesEditor.apply();
    }

    /**
     * Handles the onClick for the Reset button.  Resets the global count and
     * background variables to the defaults and resets the views to those
     * default values.
     *
     * @param view The view (Button) that was clicked.
     */
    private void reset(View view) {
        // Reset fullname
        myFullname = "";
        txtFullname.setText(String.format("%s", myFullname));

        // Clear preferences
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.remove("FULLNAME_KEY");
        preferencesEditor.commit();

        finish();

        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }
}