package com.example.demo;

import android.provider.BaseColumns;

public class TripContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private TripContract() {}

    /* Inner class that defines the table contents */
    public static class TripTable implements BaseColumns {
        public static final String TABLE_NAME = "Trip";
        public static final String COLUMN_NAME_MAIN_LOCATION = "Main_Location";
        public static final String COLUMN_NAME_START_DATE = "Start_Date";
    }

    /* Inner class that defines the table contents */
    public static class LocationTable implements BaseColumns {
        public static final String TABLE_NAME = "Location";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_TIME = "Time";
        public static final String COLUMN_NAME_TRIP_ID = "Trip_Id";
    }

    public static class NoteTable implements BaseColumns{
        public static final String TABLE_NAME = "Note";
        public static final String COLUMN_NAME_TITLE = "Title";
        public static final String COLUMN_NAME_TEXT = "Text";
    }
}
