package com.example.kiddo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {
    private static SharedPreferences prefs;
    private String sharedPrefFile =
            "com.example.android.hellosharedprefs";
    Context c;
    ArrayList<Model> models;

    public MyAdapter(Context c, ArrayList<Model> models) {
        this.c = c;
        this.models = models;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, null);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        myHolder.txtTitle.setText(models.get(i).getTitle());
        myHolder.txtSubtitle.setText(models.get(i).getSubtitle());

        myHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Integer gId = models.get(position).getId();
                prefs = c.getSharedPreferences(sharedPrefFile,MODE_PRIVATE);
                Intent intent = new Intent(c, SubActivity.class);
                intent.putExtra("MAIN_ID", gId);
                intent.putExtra("MY_FULLNAME", prefs.getString("fullname", ""));
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
