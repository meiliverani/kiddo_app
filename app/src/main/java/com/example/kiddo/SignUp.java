package com.example.kiddo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.Calendar;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignUp";

    TextInputEditText txtFullname, txtUsername, txtPassword, txtChFullname, txtChUsername, txtChPassword;
    EditText txtChDob;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    Button btnSignUp;
    TextView txtViewLogin;
    String chDob, radioButtonGender;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtFullname = findViewById(R.id.fullname);
        txtUsername = findViewById(R.id.username);
        txtPassword = findViewById(R.id.password);
        txtChFullname = findViewById(R.id.ch_fullname);
        txtChUsername = findViewById(R.id.ch_username);
        txtChPassword = findViewById(R.id.ch_password);
        txtChDob = findViewById(R.id.ch_dob);
        btnSignUp = findViewById(R.id.buttonSignUp);
        txtViewLogin = findViewById(R.id.loginText);
        progressBar = findViewById(R.id.progress);

        txtViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        txtChDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SignUp.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG,"onDateSet: yyyy/mm/dd: "+ year + "/" + month + "/" + dayOfMonth);

                String date = year + "/" + month + "/" + dayOfMonth;
                txtChDob.setText(date);
                chDob = date;
            }
        };

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullname, username, password, ch_fullname, ch_username, ch_password, ch_gender, ch_dob;
                fullname = String.valueOf(txtFullname.getText());
                username = String.valueOf(txtUsername.getText());
                password = String.valueOf(txtPassword.getText());
                ch_fullname = String.valueOf(txtChFullname.getText());
                ch_username = String.valueOf(txtChUsername.getText());
                ch_password = String.valueOf(txtChPassword.getText());
                ch_gender = radioButtonGender;
                ch_dob = chDob;

                if(!fullname.equals("") && !username.equals("") && !password.equals("") && !ch_fullname.equals("")
                        && !ch_username.equals("") && !ch_password.equals("") && !ch_gender.equals("") && !ch_dob.equals("")) {
                    //Start ProgressBar first (Set visibility VISIBLE)
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[8];
                            field[0] = "pr_fullname";
                            field[1] = "pr_username";
                            field[2] = "pr_password";
                            field[3] = "ch_fullname";
                            field[4] = "ch_username";
                            field[5] = "ch_password";
                            field[6] = "ch_gender";
                            field[7] = "ch_dob";
                            //Creating array for data
                            String[] data = new String[8];
                            data[0] = fullname;
                            data[1] = username;
                            data[2] = password;
                            data[3] = ch_fullname;
                            data[4] = ch_username;
                            data[5] = ch_password;
                            data[6] = ch_gender;
                            data[7] = ch_dob;
                            PutData putData = new PutData(Configuration.URL_SIGNUP, "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("Sign Up Success")) {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_male:
                if (checked)
                    radioButtonGender = "M";
                break;
            case R.id.radio_female:
                if (checked)
                    radioButtonGender = "F";
                break;
        }
    }
}