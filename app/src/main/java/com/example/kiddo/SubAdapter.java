package com.example.kiddo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class SubAdapter extends RecyclerView.Adapter<SubHolder> {
    private static SharedPreferences prefs;
    private String sharedPrefFile =
            "com.example.android.hellosharedprefs";
    Context c;
    ArrayList<SubModel> models;

    public SubAdapter(Context c, ArrayList<SubModel> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public SubHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_sub, null);

        return new SubHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubHolder myHolder, int i) {
        myHolder.txtActivity.setText(models.get(i).getActivity());
        myHolder.txtDesc.setText(models.get(i).getDesc());
        myHolder.txtAlarmDays.setText(models.get(i).getAlarmDays());
        myHolder.txtAlarmTime.setText(models.get(i).getAlarmTime());
        myHolder.txtStarsGiven.setText(models.get(i).getStarsGiven());
//        myHolder.txtIcon.setImageResource(models.get(i).getActivityIcon());

        prefs = c.getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
        if (prefs.getString("role", "").contains("Parent")) {
            myHolder.btnFinish.setVisibility(View.GONE);
        };

        String [] alarmTime = models.get(i).getAlarmTime().split(":");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarmTime[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(alarmTime[1]));
        cal.set(Calendar.SECOND, 0);

        if (cal.after(Calendar.getInstance())) {
            myHolder.btnFinish.setVisibility(View.GONE);
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
                        prefs = c.getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
                        params.put("username", prefs.getString("fullname", ""));
                        params.put("stars_given",myHolder.txtStarsGiven.getText().toString().trim());

                        RequestHandler rh = new RequestHandler();
                        String res = rh.sendPostRequest(Configuration.URL_UPDATE_CHILD_STARS, params);
                        return res;
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

    public SubModel getWordAtPosition (int position) {
        return models.get(position);
    }
}
