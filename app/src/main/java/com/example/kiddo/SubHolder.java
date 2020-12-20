package com.example.kiddo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubHolder extends RecyclerView.ViewHolder {
    TextView txtActivity, txtDesc, txtAlarmDays, txtAlarmTime, txtStarsGiven;
//    ImageView txtIcon;
//    ItemClickListener itemClickListener;


    SubHolder(@NonNull View itemView) {
        super(itemView);

        this.txtActivity = itemView.findViewById(R.id.subActivity);
        this.txtDesc = itemView.findViewById(R.id.subDesc);
        this.txtAlarmDays = itemView.findViewById(R.id.alarm_days);
        this.txtAlarmTime = itemView.findViewById(R.id.alarm_time);
        this.txtStarsGiven = itemView.findViewById(R.id.stars_given);
//        this.txtIcon = itemView.findViewById(R.id.activity_icon);

//        itemView.setOnClickListener(this);
    }
}
