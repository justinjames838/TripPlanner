package com.example.demo;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class LocationViewHolder extends RecyclerView.ViewHolder {
    CardView parent;
    TextView locationName,locationDateTime;
    Button locationDelete;

    public LocationViewHolder(@NonNull View itemView) {
        super(itemView);
        this.parent = itemView.findViewById(R.id.parent);
        this.locationName = itemView.findViewById(R.id.location_name);
        this.locationDateTime = itemView.findViewById(R.id.location_date_time);
        this.locationDelete = itemView.findViewById(R.id.location_delete);
    }
}
