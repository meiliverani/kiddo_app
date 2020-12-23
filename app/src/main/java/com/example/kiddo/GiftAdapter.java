package com.example.kiddo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class GiftAdapter extends RecyclerView.Adapter<GiftHolder> {
    private static SharedPreferences prefs;
    private String sharedPrefFile =
            "com.example.android.hellosharedprefs";
    Context c;
    Integer id;
    String stars_needed;
    ArrayList<GiftModel> models;

    public GiftAdapter(Context c, ArrayList<GiftModel> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public GiftHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_gift, null);

        return new GiftHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftHolder myHolder, int i) {
        id = models.get(i).getId();
        stars_needed = models.get(i).getStars_required();
        myHolder.giftName.setText(models.get(i).getName());
        myHolder.giftDesc.setText(models.get(i).getDescription());
        myHolder.giftStars.setText(models.get(i).getStars_required());

        prefs = c.getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
        if (prefs.getString("role", "").contains("Parent")) {
            myHolder.buttonRedeem.setVisibility(View.GONE);
        } else {
            if (models.get(i).getIs_redeemed().equals("1") | Integer.parseInt(prefs.getString("stars", "")) < Integer.parseInt(models.get(i).getStars_required())) {
                myHolder.buttonRedeem.setVisibility(View.GONE);
            }
        }

        myHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                class GetMainActivities extends AsyncTask<Void,Void,String> {
                    ProgressDialog loading;
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        loading.dismiss();
                    }

                    @Override
                    protected String doInBackground(Void... v) {
                        HashMap<String,String> params = new HashMap<>();
                        HashMap<String,String> params_ = new HashMap<>();
                        params.put("id", id.toString());
                        params_.put("stars_needed",stars_needed);
                        params_.put("username",prefs.getString("fullname", ""));

                        RequestHandler rh = new RequestHandler();
                        String res = rh.sendPostRequest(Configuration.URL_UPDATE_GIFTS, params);
                        RequestHandler rh_ = new RequestHandler();
                        String res_ = rh_.sendPostRequest(Configuration.URL_UPDATE_CHILD_STAR_REDEEM, params_);
                        return res+" | "+res_;
                    }
                }
                GetMainActivities ge = new GetMainActivities();
                ge.execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public GiftModel getWordAtPosition (int position) {
        return models.get(position);
    }
}
