package com.example.kiddo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;

public class Gift extends AppCompatActivity implements View.OnClickListener {
    private String myFullname, myRole, myStars = "";
    private TextView txtFullname, txtStarsCollected;
    private Button btnAddGift;
    private RelativeLayout profileRelativeLayout;

    // Key for current fullname
    private final String FULLNAME_KEY = "fullname";

    // Shared preferences object
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    // Name of shared preferences file
    private String sharedPrefFile =
            "com.example.android.hellosharedprefs";

    RecyclerView recyclerView;
    GiftAdapter myAdapter;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);

        txtFullname = (TextView) findViewById(R.id.myFullname);
        txtStarsCollected = (TextView) findViewById(R.id.profile_stars);
        btnAddGift = (Button) findViewById(R.id.buttonAddGift);
        profileRelativeLayout = findViewById(R.id.profileRelativeLayout);

        String _myFullname =  getIntent().getStringExtra("MY_FULLNAME");
        String _myRole =  getIntent().getStringExtra("MY_ROLE");
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        mEditor = mPreferences.edit();
        mEditor.putString(FULLNAME_KEY, _myFullname);
        mEditor.putString("role", _myRole);
        if (!myStars.equals("") ) {mEditor.putString("stars", myStars);}
        mEditor.commit();

        myFullname = mPreferences.getString(FULLNAME_KEY, _myFullname);
        myRole = mPreferences.getString("role", _myRole);

        txtFullname.setText(String.format("%s", myFullname));

        if (myRole.contains("Child")) {
            btnAddGift.setVisibility(View.GONE);
            findViewById(R.id.deleteHint).setVisibility(View.GONE);
        }

        if (myRole.contains("Parent")) {
            profileRelativeLayout.setVisibility(View.GONE);
            btnAddGift.setOnClickListener(this);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.gift);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        Intent intent = new Intent(Gift.this, Profile.class);
                        intent.putExtra("MY_FULLNAME", myFullname);
                        intent.putExtra("MY_ROLE", myRole);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.home:
                        Intent intent_ = new Intent(Gift.this, MainActivity.class);
                        intent_.putExtra("MY_FULLNAME", myFullname);
                        intent_.putExtra("MY_ROLE", myRole);
                        startActivity(intent_);
                        overridePendingTransition(0,0);
                        return  true;
                    case R.id.gift:
                        return  true;
                }
                return false;
            }
        });

        requestQueue = Volley.newRequestQueue(this);

        getChildProfile();

        getMainActivities();
        setLayoutAdapter();

        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        if (myRole.contains("Child")) {
                            Toast.makeText(Gift.this, "You have no permission to delete gift.", Toast.LENGTH_LONG).show();
                        } else {
                            int position = viewHolder.getAdapterPosition();
                            GiftModel myGift = myAdapter.getWordAtPosition(position);
                            Toast.makeText(Gift.this, "Deleting " +
                                    myGift.getName(), Toast.LENGTH_LONG).show();

                            // Delete the word
                            confirmDeleteGift(myGift.getId());
                        }
                    }
                });

        helper.attachToRecyclerView(recyclerView);
    }

    public void setLayoutAdapter() {
        recyclerView = findViewById(R.id.recyclerViewGift);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAdapter = new GiftAdapter(this, getMyList());
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(myAdapter);
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
                        String stars_collected = jsonObject.optString("stars_collected");
                        if (username.equals(myFullname)) {
                            myStars = stars_collected;
                            mEditor = mPreferences.edit();
                            if (!myStars.equals("") ) {mEditor.putString("stars", myStars);}
                            mEditor.commit();
                            txtStarsCollected.setText(stars_collected);
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

    private void getMainActivities(){
        class GetMainActivities extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Gift.this,"Fetching...","Wait...",false,false);
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
                String s = rh.sendGetRequest(Configuration.URL_GET_ALL_GIFTS);
                return s;
            }
        }
        GetMainActivities ge = new GetMainActivities();
        ge.execute();
    }

    private ArrayList<GiftModel> getMyList() {
        final ArrayList<GiftModel> models = new ArrayList<>();

        String url = Configuration.URL_GET_ALL_GIFTS;
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
                            String name = jsonObject.optString("name");
                            String desc = jsonObject.optString("description");
                            String stars_required = jsonObject.optString("stars_required");
                            String is_redeemed = jsonObject.optString("is_redeemed");
                            GiftModel m = new GiftModel();
                            m.setId(id);
                            m.setName(name);
                            m.setDescription(desc);
                            m.setStars_required(stars_required);
                            m.setIs_redeemed(is_redeemed);
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

        return models;
    }

    private void confirmDeleteGift(final Integer id){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure to delete this Gift?");

        alertDialogBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteGift(id);
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteGift(final Integer id){
        class DeleteMain extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Gift.this, "Deleting...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Gift.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("id",id.toString());

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(Configuration.URL_DELETE_GIFTS, hashMap);
                return s;
            }
        }

        DeleteMain de = new DeleteMain();
        de.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString(FULLNAME_KEY, myFullname);
        preferencesEditor.apply();
    }

    @Override
    public void onClick(View v) {
        if (v == btnAddGift) {
            Intent intent = new Intent(Gift.this, AddGiftActivity.class);
            intent.putExtra("MY_FULLNAME", myFullname);
            startActivity(intent);
        }
    }
}