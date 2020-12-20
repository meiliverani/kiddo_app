package com.example.kiddo;

import androidx.appcompat.app.ActionBar;
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
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubActivity extends AppCompatActivity implements View.OnClickListener {
    private String myFullname, myRole = "";
    private Integer myMainId = 0;
    private Button btnAddSub;

    // Key for current fullname
    private final String FULLNAME_KEY = "fullname";

    // Shared preferences object
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    // Name of shared preferences file
    private String sharedPrefFile =
            "com.example.android.hellosharedprefs";

    RecyclerView recyclerView;
    SubAdapter myAdapter;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String _myFullname =  getIntent().getStringExtra("MY_FULLNAME");
        String _myRole =  getIntent().getStringExtra("MY_ROLE");
        Integer _myMainId =  getIntent().getIntExtra("MAIN_ID", 0);

        btnAddSub = (Button) findViewById(R.id.buttonAddSub);
        btnAddSub.setOnClickListener(this);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.putString(FULLNAME_KEY, _myFullname);
        mEditor.commit();

        // Restore preferences
        myFullname = mPreferences.getString(FULLNAME_KEY, _myFullname);
        myRole = mPreferences.getString("role", _myRole);
        myMainId = _myMainId;

        if (myRole.contains("Child")) {
            btnAddSub.setVisibility(View.GONE);
        }

        requestQueue = Volley.newRequestQueue(this);

        getSubActivities();
        setLayoutAdapter();
    }

    public void setLayoutAdapter() {
        recyclerView = findViewById(R.id.recyclerViewSub);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new SubAdapter(this, getMyList());
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(myAdapter);
    }

    private void getSubActivities(){
        class GetSubActivities extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SubActivity.this,"Fetching...","Wait...",false,false);
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
                String s = rh.sendGetRequest(Configuration.URL_GET_ALL_SUB_ACTIVITIES);
                return s;
            }
        }
        GetSubActivities ge = new GetSubActivities();
        ge.execute();
    }

    private ArrayList<SubModel> getMyList() {
        final ArrayList<SubModel> models = new ArrayList<>();

        String url = Configuration.URL_GET_ALL_SUB_ACTIVITIES;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("result");
                    for(int i=0; i<jsonArray.length();i++) {
                        JSONObject jsonObject =jsonArray.getJSONObject(i);
                        Integer main_id = jsonObject.optInt("main_id");
                        if (main_id==myMainId) {
                            Integer id = jsonObject.optInt("id");
                            String activity = jsonObject.optString("activity");
                            String alarm_desc = jsonObject.optString("alarm_desc");
                            String days = jsonObject.optString("days");
                            String time = jsonObject.optString("time");
                            String stars = jsonObject.optString("stars");
                            SubModel m = new SubModel();
                            m.setActivity(activity);
                            m.setDesc(alarm_desc);
                            m.setAlarmDays(days);
                            m.setAlarmTime(time);
                            m.setStarsGiven(stars);
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

//
//        SubModel m = new SubModel();
//        m.setActivity("Test Activity");
//        m.setAlarmDays("Senin, Selasa, Rabu");
//        m.setAlarmTime("07:00");
//        m.setDesc("Test Desc");
//        m.setStarsGiven("88");
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

    @Override
    public void onClick(View v) {
        if (v == btnAddSub) {
            Intent intent = new Intent(SubActivity.this, AddSubActivity.class);
            intent.putExtra("MAIN_ID", myMainId);
            startActivity(intent);
        }
    }

}
