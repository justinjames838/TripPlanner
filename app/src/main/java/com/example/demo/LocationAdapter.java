package com.example.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationViewHolder> {

    private List<Location> locationList;
    private Context context;
    private boolean hideDelete;

    public LocationAdapter(List<Location> locationList, Context context,boolean hideDelete) {
        this.locationList = locationList;
        this.context = context;
        this.hideDelete = hideDelete;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_recycle_item, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location loc = locationList.get(position);
        holder.locationName.setText(loc.getName());
        holder.locationDateTime.setText(loc.getTime());

        holder.locationDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationList.remove(position);
                LocationAdapter.this.notifyItemRemoved(position);
                LocationAdapter.this.notifyItemRangeChanged(position, locationList.size());
            }
        });

        if(hideDelete){
            holder.locationDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }
}
