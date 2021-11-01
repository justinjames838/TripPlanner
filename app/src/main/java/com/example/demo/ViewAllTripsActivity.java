package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ViewAllTripsActivity extends AppCompatActivity implements AdapterCallbacks {

    TripDbHelper db;
    RecyclerView trip_Recycle;
    TextView no_upcoming_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_trips);

        no_upcoming_text = findViewById(R.id.no_upcoming_Text);

        trip_Recycle = findViewById(R.id.trip_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        trip_Recycle.setLayoutManager(linearLayoutManager);

        db = new TripDbHelper(this);
        ArrayList<Trip> allTrips = db.listTrips();
//        System.out.println(allTrips.size());
        Collections.sort(allTrips, new Comparator<Trip>() {
            @Override
            public int compare(Trip o1, Trip o2) {
                Date d1=null,d2=null;
                try {
                    d1 = new SimpleDateFormat("yyyy-MM-dd").parse(o1.getStartDate());
                    d2 = new SimpleDateFormat("yyyy-MM-dd").parse(o2.getStartDate());
                }catch (Exception e){
                    Toast.makeText(ViewAllTripsActivity.this,"Error in reading dates",Toast.LENGTH_LONG);
                    startActivity(new Intent(ViewAllTripsActivity.this,MainActivity.class));
                }

                return d1.compareTo(d2);
            }
        });
        if (allTrips.size() > 0) {
            trip_Recycle.setVisibility(View.VISIBLE);
            no_upcoming_text.setVisibility(View.GONE);
            TripAdapter tAdapter = new TripAdapter(this, allTrips,db);
//            tAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//                @Override
//                public void onChanged() {
//                    super.onChanged();
//                    checkAdapterIsEmpty(tAdapter.getItemCount());
//                }
//            });
            trip_Recycle.setAdapter(tAdapter);

        }
        else {
            trip_Recycle.setVisibility(View.GONE);
            no_upcoming_text.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void checkAdapterIsEmpty(int size) {
        if(size == 0){
            trip_Recycle.setVisibility(View.GONE);
            no_upcoming_text.setVisibility(View.VISIBLE);
        }
    }
}