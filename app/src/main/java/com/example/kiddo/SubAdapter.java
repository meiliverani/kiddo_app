package com.example.kiddo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SubAdapter extends RecyclerView.Adapter<SubHolder> {
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
    public void onBindViewHolder(@NonNull SubHolder myHolder, int i) {
        myHolder.txtActivity.setText(models.get(i).getActivity());
        myHolder.txtDesc.setText(models.get(i).getDesc());
        myHolder.txtAlarmDays.setText(models.get(i).getAlarmDays());
        myHolder.txtAlarmTime.setText(models.get(i).getAlarmTime());
        myHolder.txtStarsGiven.setText(models.get(i).getStarsGiven());
//        myHolder.txtIcon.setImageResource(models.get(i).getActivityIcon());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
