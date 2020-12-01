package com.example.kiddo;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder {

    TextView txtTitle, txtSubtitle;

    public MyHolder(@NonNull View itemView) {
        super(itemView);

        this.txtTitle = itemView.findViewById(R.id.mainTitle);
        this.txtSubtitle = itemView.findViewById(R.id.mainSubtitle);
    }
}
