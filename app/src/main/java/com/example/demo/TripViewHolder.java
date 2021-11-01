package com.example.demo;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TripViewHolder extends RecyclerView.ViewHolder {
    CardView parent;
    TextView tripName,tripDate;
    Button tripDetails,tripDelete;

    public TripViewHolder(@NonNull View itemView) {
        super(itemView);
        this.parent = itemView.findViewById(R.id.parent);
        this.tripName = itemView.findViewById(R.id.trip_name);
        this.tripDate = itemView.findViewById(R.id.trip_date);
        this.tripDetails = itemView.findViewById(R.id.trip_details_button);
        this.tripDelete = itemView.findViewById(R.id.trip_delete);
    }
}
