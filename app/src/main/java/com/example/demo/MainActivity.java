package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button addTrip,ViewTrips,note;
    TripDbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = db = new TripDbHelper(this);
        note = findViewById(R.id.notesBtn);

        addTrip = (Button) findViewById(R.id.addTripButton);

        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,AddTripActivity.class);
                startActivity(i);
            }
        });

        ViewTrips = (Button)findViewById(R.id.upcomingTripBtn);

        ViewTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ViewAllTripsActivity.class);
                startActivity(i);
            }
        });

        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ViewAllNotesActivity.class));
            }
        });
    }




}