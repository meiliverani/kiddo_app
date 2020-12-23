package com.example.kiddo;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GiftHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView giftName, giftDesc, giftStars;
    ItemClickListener itemClickListener;
    Button buttonRedeem;

    public GiftHolder(@NonNull View itemView) {
        super(itemView);

        this.giftName = itemView.findViewById(R.id.giftName);
        this.giftDesc = itemView.findViewById(R.id.giftDesc);
        this.giftStars = itemView.findViewById(R.id.giftStars);
        this.buttonRedeem = itemView.findViewById(R.id.buttonRedeem);

        this.buttonRedeem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClickListener(v, getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener ic) {
        this.itemClickListener = ic;
    }
}
