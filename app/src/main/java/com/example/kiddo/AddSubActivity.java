package com.example.kiddo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;
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
import java.util.Calendar;
import java.util.HashMap;

public class AddSubActivity extends AppCompatActivity implements View.OnClickListener {
    private Integer myMainId = 0;

    private EditText txtAlarmDesc, txtPoints, txtAlarmTime;
    private Button buttonSaveNewSub;
    private TimePickerDialog timePickerDialog;

    Spinner spinnerActivities;
    String alarmTime;

    private TimePickerDialog.OnTimeSetListener onTimeSetListener;

    ArrayList<String> daysList = new ArrayList<>();
    ArrayList<String> activitiesList = new ArrayList<>();
    ArrayList<String> activityIDList = new ArrayList<>();
    ArrayAdapter<String> activityAdapter;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub);

        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Integer _myMainId =  getIntent().getIntExtra("MAIN_ID", 0);

        myMainId = _myMainId;

        txtAlarmDesc = (EditText) findViewById(R.id.txtAlarmDesc);
        txtPoints = (EditText) findViewById(R.id.txtPoints);
        txtAlarmTime = (EditText) findViewById(R.id.txtAlarmTime);

        buttonSaveNewSub = (Button) findViewById(R.id.buttonSaveNewSub);
        buttonSaveNewSub.setOnClickListener(this);

        requestQueue = Volley.newRequestQueue(this);
        spinnerActivities = findViewById(R.id.spinnerActivities);

        txtAlarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                timePickerDialog = new TimePickerDialog(AddSubActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        alarmTime = hourOfDay + ":" + minute;
                        txtAlarmTime.setText(alarmTime);
                    }
                },
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                        DateFormat.is24HourFormat(AddSubActivity.this));
                timePickerDialog.show();
            }
        });

        String url = Configuration.URL_GET_ALL_ACTIVITIES;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("result");
                    for (int i=0; i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String act = jsonObject.optString("name");
                        String actID = jsonObject.optString("id");
                        activitiesList.add(act);
                        activityIDList.add(actID);
                        activityAdapter = new ArrayAdapter<>(AddSubActivity.this, android.R.layout.simple_spinner_item, activitiesList);
                        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerActivities.setAdapter(activityAdapter);
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

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_mon:
                if (checked)
                    daysList.add("Monday");
                break;
            case R.id.checkbox_tue:
                if (checked)
                    daysList.add("Tuesday");
                break;
            case R.id.checkbox_wed:
                if (checked)
                    daysList.add("Wednesday");
                break;
            case R.id.checkbox_thu:
                if (checked)
                    daysList.add("Thursday");
                break;
            case R.id.checkbox_fri:
                if (checked)
                    daysList.add("Friday");
                break;
            case R.id.checkbox_sat:
                if (checked)
                    daysList.add("Saturday");
                break;
            case R.id.checkbox_sun:
                if (checked)
                    daysList.add("Sunday");
                break;
        }
    }

    private void addSubAct() {
        final String alarmDesc = txtAlarmDesc.getText().toString().trim();
        final String points = txtPoints.getText().toString().trim();
        final String alarmDays = android.text.TextUtils.join(",", daysList);
        final String activityID = activityIDList.get((int) spinnerActivities.getSelectedItemId()).trim();
        final String main_id = myMainId.toString().trim();

        class AddSubAct extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddSubActivity.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(AddSubActivity.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("activity_id",activityID);
                params.put("main_id",main_id);
                params.put("alarm_description",alarmDesc);
                params.put("days",alarmDays);
                params.put("time",alarmTime);
                params.put("stars_given",points);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Configuration.URL_ADD_SUB_ACTIVITY, params);
                return res;
            }
        }
        AddSubAct amc = new AddSubAct();
        amc.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSaveNewSub) {
            addSubAct();
        }
    }
}