package com.example.kiddo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.HashMap;

public class AddGiftActivity extends AppCompatActivity implements View.OnClickListener {

    private String myFullname = "";
    private SharedPreferences mPreferences;
    private String sharedPrefFile =
            "com.example.android.hellosharedprefs";

    private Button buttonSaveNewGift;

    private EditText editTextName;
    private EditText editTextDesc;
    private EditText editTextStars;

    Spinner spinnerChildren;
    ArrayList<String> childrenList = new ArrayList<>();
    ArrayAdapter<String> childrenAdapter;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gift);

        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonSaveNewGift = (Button) findViewById(R.id.buttonSaveNewGift);
        buttonSaveNewGift.setOnClickListener(this);

        editTextName =(EditText) findViewById(R.id.txtGiftName);
        editTextDesc =(EditText) findViewById(R.id.txtGiftDesc);
        editTextStars =(EditText) findViewById(R.id.txtStars);

        requestQueue = Volley.newRequestQueue(this);
        spinnerChildren = findViewById(R.id.spinnerChildren);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        myFullname = mPreferences.getString("fullname", "");

        String url = Configuration.URL_GET_ALL_PARENT_CHILDREN;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("result");
                    for (int i=0; i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String parent_username = jsonObject.optString("parent_username");
                        String kid_username = jsonObject.optString("kid_username");
                        if (parent_username.equals(myFullname)) {
                            childrenList.add(kid_username);
                            childrenAdapter = new ArrayAdapter<>(AddGiftActivity.this, android.R.layout.simple_spinner_item, childrenList);
                            childrenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerChildren.setAdapter(childrenAdapter);
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
    }
    private void addMainAct() {
        final String name = editTextName.getText().toString().trim();
        final String desc = editTextDesc.getText().toString().trim();
        final String stars = editTextStars.getText().toString().trim();
        final String child_username = spinnerChildren.getSelectedItem().toString().trim();

        class AddMainAct extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddGiftActivity.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(AddGiftActivity.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("name",name);
                params.put("desc",desc);
                params.put("stars_required",stars);
                params.put("created_for",child_username);
                params.put("created_by",myFullname);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Configuration.URL_ADD_GIFTS, params);
                return res;
            }
        }
        AddMainAct amc = new AddMainAct();
        amc.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSaveNewGift) {
            addMainAct();
        }
    }
}