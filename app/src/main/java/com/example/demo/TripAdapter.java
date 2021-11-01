package com.example.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripViewHolder>{
    private TripDbHelper db;
    private List<Trip> tripList;
    private Context context;
    private AdapterCallbacks adapterCallBack;

    public TripAdapter(Context context, List<Trip> tripList, TripDbHelper db) {
        this.db = db;
        this.tripList = tripList;
        this.context = context;
        this.adapterCallBack = (AdapterCallbacks)context;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_recycle_item, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        holder.tripName.setText(trip.getMainLocation());
        holder.tripDate.setText(trip.getStartDate());
        holder.tripDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(context,EditAndViewTripActivity.class);
                    i.putExtra("Destination",trip.getMainLocation());
                    i.putExtra("Date",trip.getStartDate());
                    i.putExtra("Locations",(Serializable)trip.getLocations());
                ((ViewAllTripsActivity)context).startActivity(i);
            }
        });
        holder.tripDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               db.deleteTrip(trip.getTripId());
                tripList.remove(position);
                TripAdapter.this.notifyItemRemoved(position);
                TripAdapter.this.notifyItemRangeChanged(position, tripList.size());
                adapterCallBack.checkAdapterIsEmpty(tripList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }
}
