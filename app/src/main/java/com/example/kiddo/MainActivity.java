package com.example.kiddo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String myFullname, myRole = "";
    private TextView txtFullname;
    private Button btnAddMain;

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

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String _myFullname =  getIntent().getStringExtra("MY_FULLNAME");
        String _myRole =  getIntent().getStringExtra("MY_ROLE");
        txtFullname = (TextView) findViewById(R.id.myFullname);
        btnAddMain = (Button) findViewById(R.id.buttonAddMain);
        btnAddMain.setOnClickListener(this);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.putString(FULLNAME_KEY, _myFullname);
        mEditor.putString("role", _myRole);
        mEditor.commit();


        // Restore preferences
        myFullname = mPreferences.getString(FULLNAME_KEY, _myFullname);
        myRole = mPreferences.getString("role", _myRole);
        txtFullname.setText(String.format("%s", myFullname));
        if (myRole.contains("Child")) {
            btnAddMain.setVisibility(View.GONE);
        }

        requestQueue = Volley.newRequestQueue(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        Intent intent = new Intent(MainActivity.this, Profile.class);
                        intent.putExtra("MY_FULLNAME", myFullname);
                        intent.putExtra("MY_ROLE", myRole);
                        startActivity(intent);
//                        startActivity(new Intent(MainActivity.this, Profile.class));
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.home:
                        return  true;
                    case R.id.gift:
                        Intent intent_ = new Intent(MainActivity.this, Gift.class);
                        intent_.putExtra("MY_FULLNAME", myFullname);
                        intent_.putExtra("MY_ROLE", myRole);
                        startActivity(intent_);
//                        startActivity(new Intent(MainActivity.this, Gift.class));
                        overridePendingTransition(0,0);
                        return  true;
                }
                return false;
            }
        });

        getMainActivities();
        setLayoutAdapter();
    }

    public void setLayoutAdapter() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new MyAdapter(this, getMyList());
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);

                builder.setMessage("Thank you for using KidDo App! ♡ \n\nAre you sure you want to Log out? \n")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logOut();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        reset(txtFullname);
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }

    private void getMainActivities(){
        class GetMainActivities extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                getMyList();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Configuration.URL_GET_ALL_MAIN_ACTIVITIES);
                return s;
            }
        }
        GetMainActivities ge = new GetMainActivities();
        ge.execute();
    }

    private ArrayList<Model> getMyList() {
        final ArrayList<Model> models = new ArrayList<>();

        String url = Configuration.URL_GET_ALL_MAIN_ACTIVITIES;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("result");
                    for(int i=0; i<jsonArray.length();i++) {
                        JSONObject jsonObject =jsonArray.getJSONObject(i);
                        String created_for = jsonObject.optString("created_for");
                        String created_by = jsonObject.optString("created_by");
                        if (created_by.equals(myFullname)  || created_for.equals(myFullname)) {
                            Integer id = jsonObject.optInt("id");
                            String title = jsonObject.optString("title");
                            String subtitle = jsonObject.optString("subtitle");
                            Model m = new Model();
                            m.setId(id);
                            m.setTitle(title);
                            m.setSubtitle(subtitle);
                            models.add(m);
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

        requestQueue.add(jsonObjectRequest);


//        Model m = new Model();
//        m.setTitle("Test dulu pertmaa ya");
//        m.setSubtitle("ini juga harus test panjang panjang bair ngena giuuuu");
//        models.add(m);
//
//        m = new Model();
//        m.setTitle("test kedua");
//        m.setSubtitle("ini jugaa kedua");
//        models.add(m);

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

    @Override
    public void onClick(View v) {
        if (v == btnAddMain) {
            Intent intent = new Intent(MainActivity.this, AddMainActivity.class);
            intent.putExtra("MY_FULLNAME", myFullname);
            startActivity(intent);
        }
    }
}