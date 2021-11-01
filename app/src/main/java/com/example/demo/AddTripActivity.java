package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddTripActivity extends AppCompatActivity {

    RecyclerView loc_recycler;
    TextView new_loc_date,new_loc_time;
    Button new_loc_date_picker,new_loc_time_picker,add_new_location,save_trip,cancel;
    EditText destination,new_location_name;
    CalendarView trip_date;
    ArrayList<Location> loclist;
    LocationAdapter adapter;
    TripDbHelper db;
    Calendar calendar;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        calendar = Calendar.getInstance();

        db = new TripDbHelper(this);

        loclist = new ArrayList<>();

        loc_recycler = (RecyclerView)findViewById(R.id.locationRecycler);

        new_loc_date = (TextView)findViewById(R.id.newLocationDate);
        new_loc_time = (TextView)findViewById(R.id.newLocationTime);

        destination = (EditText)findViewById(R.id.mainLocationText);
        new_location_name = (EditText)findViewById(R.id.newLocationName);

        new_loc_date_picker = (Button)findViewById(R.id.new_loc_date_dialog);
        new_loc_time_picker = (Button)findViewById(R.id.new_loc_time_dialog);
        add_new_location = (Button)findViewById(R.id.addLocationButton);
        save_trip = (Button)findViewById(R.id.addTripButton);
        cancel = (Button)findViewById(R.id.trip_cancel_button);

        trip_date = (CalendarView)findViewById(R.id.trip_calendar);
        trip_date.setMinDate(calendar.getTimeInMillis());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        loc_recycler.setLayoutManager(linearLayoutManager);
        adapter = new LocationAdapter(loclist,this,false);

        loc_recycler.setVisibility(View.VISIBLE);
        loc_recycler.setAdapter(adapter);

        new_loc_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTripActivity.this,
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTripActivity.this,
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

        add_new_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!new_location_name.getText().toString().equals("") && !new_loc_date.getText().toString().equals("YYYY-MM-DD") && !new_loc_time.getText().toString().equals("HH:MM:SS")) {
                   Location loc = new Location(0, 0, new_location_name.getText().toString(), new_loc_date.getText().toString() + " " + new_loc_time.getText().toString());
                   loclist.add(loc);
                   adapter.notifyItemInserted(adapter.getItemCount()-1);
                }
            }
        });

        trip_date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth,10,45,0);

            }
        });

        save_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!destination.getText().toString().equals("") && loclist.size()!=0){
                    String date = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date(calendar.getTimeInMillis()));
                    if(db.addTrip(new Trip(0,destination.getText().toString(),date,loclist)) == -1){
                        Toast.makeText(AddTripActivity.this,"Trip Already exists",Toast.LENGTH_LONG).show();

                    }
                    else{
                        Toast.makeText(AddTripActivity.this,"Trip added Successfully",Toast.LENGTH_LONG).show();
                        scheduleNotification(getNotification("Your trip is to "+destination.getText().toString()+" is about to start "),calendar.getTimeInMillis());
                        resetInputs();
                    }

                }
                else{
                    if(destination.getText().toString().equals("")){
                        Toast.makeText(AddTripActivity.this,"Please specify a destination",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(AddTripActivity.this,"Please add at least one location",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetInputs();
                startActivity(new Intent(AddTripActivity.this,MainActivity.class));
            }
        });




//        loc = (TextView) findViewById(R.id.locationText);
//        date = (TextView) findViewById(R.id.dateText);
//        other = (TextView) findViewById(R.id.otherText);
//
//        TripDbHelper db = new TripDbHelper(getApplicationContext());
//        List<Location> locations = new ArrayList<>();
//        locations.add(new Location(0,0,"Central Mall","10:00pm"));
//        Trip trip = new Trip(0,"Bengaluru","2021-04-31",locations );
//        if(db.addTrip(trip) == -1){
//            Toast.makeText(this,"Duplicate Trip "+ trip.getMainLocation(),Toast.LENGTH_SHORT);
//        };
//
//        locations = new ArrayList<>();
//        locations.add(new Location(0,0,"Gateway of India","5:00pm"));
//        trip = new Trip(0,"Mumbai","2021-05-30",locations);
//        if(db.addTrip(trip) == -1){
//            Toast.makeText(this,"Duplicate Trip "+ trip.getMainLocation(),Toast.LENGTH_SHORT);
//        };




    }

    private void scheduleNotification (Notification notification , long delay) {
        Intent notificationIntent = new Intent( this, MyNotificationPublisher. class ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.setExact(AlarmManager. ELAPSED_REALTIME_WAKEUP , delay , pendingIntent); ;
    }
    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, default_notification_channel_id);
        builder.setContentTitle("You have a trip");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }
    private void resetInputs(){
        int size = adapter.getItemCount();
        new_loc_date.setText("YYYY-MM-DD");
        new_loc_time.setText("HH:MM:SS");
        destination.getText().clear();

        loclist.clear();
        adapter.notifyItemRangeChanged(0,size);
    }

}