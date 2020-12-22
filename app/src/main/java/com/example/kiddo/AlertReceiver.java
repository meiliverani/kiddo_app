package com.example.kiddo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.List;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        List<String> days_list = intent.getStringArrayListExtra("ALARM_DAYS");
        for (int i=0; i<days_list.size(); i++) {
            int day_number;
            if (days_list.get(i).equals("Monday")) { day_number = 2; }
            else if (days_list.get(i).equals("Tuesday")) { day_number = 3; }
            else if (days_list.get(i).equals("Wednesday")) { day_number = 4; }
            else if (days_list.get(i).equals("Thursday")) { day_number = 5; }
            else if (days_list.get(i).equals("Friday")) { day_number = 6; }
            else if (days_list.get(i).equals("Saturday")) { day_number = 7; }
            else if (days_list.get(i).equals("Sunday")) { day_number = 1; }
            else day_number = 0;

            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.DAY_OF_WEEK) == day_number) {
                NotificationHelper notificationHelper =  new NotificationHelper(context);
                NotificationCompat.Builder nb = notificationHelper.getChannelNotification(intent.getStringExtra("ALARM_TITLE"), intent.getStringExtra("ALARM_DESC"));
                notificationHelper.getManager().notify(1, nb.build());
            }
        }
    }
}
