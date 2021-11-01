package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditAndViewTripActivity extends AppCompatActivity {

    Button back_to_main_menu,update_trip,edit_trip,new_loc_date_picker,new_loc_time_picker,add_new_loc;
    EditText destination,new_location_name;
    TextView date_display,new_loc_date,new_loc_time;
    CalendarView trip_date;
    RelativeLayout new_location_adder;
    RecyclerView loc_recycler;
    LocationAdapter adapter;
    List<Location> loclist;
    Calendar calendar;
    TripDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_and_view_trip);

        db = new TripDbHelper(this);

        calendar = Calendar.getInstance();

        loc_recycler = findViewById(R.id.editTripLocationRecycler);

        update_trip = findViewById(R.id.editTripSaveBtn);
        back_to_main_menu = findViewById(R.id.editTripBackTommBtn);
        edit_trip = findViewById(R.id.editTripButton);
        new_loc_date_picker = findViewById(R.id.new_loc_date_dialog);
        new_loc_time_picker = findViewById(R.id.new_loc_time_dialog);
        add_new_loc = findViewById(R.id.addLocationButton);

        new_location_adder = findViewById(R.id.edit_location_add_group);

        trip_date = findViewById(R.id.calendarView);

        date_display = findViewById(R.id.date_display);
        new_loc_date = findViewById(R.id.newLocationDate);
        new_loc_time = findViewById(R.id.newLocationTime);

        destination = findViewById(R.id.editTripDestination);
        new_location_name = findViewById(R.id.newLocationName);

        loclist = (List<Location>)getIntent().getSerializableExtra("Locations");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        loc_recycler.setLayoutManager(linearLayoutManager);
        adapter = new LocationAdapter(loclist,this,false);

        loc_recycler.setVisibility(View.VISIBLE);
        loc_recycler.setAdapter(adapter);

        destination.setText(getIntent().getStringExtra("Destination"));
        destination.setFocusable(false);
        date_display.setText(getIntent().getStringExtra("Date"));

        trip_date.setVisibility(View.GONE);

        back_to_main_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditAndViewTripActivity.this,MainActivity.class));
            }
        });

        update_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!destination.getText().toString().equals("") && loclist.size()!=0){
                    long oldTripId = db.getTripId(new Trip(0,getIntent().getStringExtra("Destination"),getIntent().getStringExtra("Date"),loclist));
                    db.updateTrip(new Trip(oldTripId,destination.getText().toString(),date_display.getText().toString(),loclist),true);
                    Toast.makeText(EditAndViewTripActivity.this,"Trip Updated",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditAndViewTripActivity.this,ViewAllTripsActivity.class));

                }
                else{
                    if(destination.getText().toString().equals("")){
                        Toast.makeText(EditAndViewTripActivity.this,"Please specify a destination",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(EditAndViewTripActivity.this,"Please add at least one location",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        edit_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    editEnable();
                    update_trip.setVisibility(View.VISIBLE);
            }
        });

        add_new_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!new_location_name.getText().toString().equals("") && !new_loc_date.getText().toString().equals("YYYY-MM-DD") && !new_loc_time.getText().toString().equals("HH:MM:SS")) {
                    Location loc = new Location(0, 0, new_location_name.getText().toString(), new_loc_date.getText().toString() + " " + new_loc_time.getText().toString());
                    loclist.add(loc);
                    adapter.notifyItemInserted(adapter.getItemCount()-1);
                }
            }
        });


        new_loc_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(EditAndViewTripActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                new_loc_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        new_loc_time_picker.setOnClickListener(new View.OnClickListener() {

            int mHour = calendar.get(Calendar.HOUR_OF_DAY);
            int mMinute = calendar.get(Calendar.MINUTE);
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditAndViewTripActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {


                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                new_loc_time.setText(hourOfDay + ":" + minute+":00.000");
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        trip_date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth,0,0,0);
                date_display.setText((new SimpleDateFormat("yyyy-MM-dd")).format(new Date(calendar.getTimeInMillis())));

            }
        });

    }

    private void editEnable(){
        destination.setFocusableInTouchMode(true);
        trip_date.setVisibility(View.VISIBLE);
        new_location_adder.setVisibility(View.VISIBLE);
    }
}