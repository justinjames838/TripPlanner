package com.example.demo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TripDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TripBuddy.db";

    private static final String SQL_CREATE_TRIPS =
            "CREATE TABLE IF NOT EXISTS " + TripContract.TripTable.TABLE_NAME + " (" +
                    TripContract.TripTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TripContract.TripTable.COLUMN_NAME_MAIN_LOCATION + " TEXT," +
                    TripContract.TripTable.COLUMN_NAME_START_DATE + " TEXT)";

    private static final String SQL_CREATE_LOCATIONS =
            "CREATE TABLE IF NOT EXISTS " + TripContract.LocationTable.TABLE_NAME + " (" +
                    TripContract.LocationTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TripContract.LocationTable.COLUMN_NAME_TRIP_ID + " INTEGER,"+
                    TripContract.LocationTable.COLUMN_NAME_NAME + " TEXT," +
                    TripContract.LocationTable.COLUMN_NAME_TIME + " TEXT)";

    private static final String SQL_CREATE_NOTES =
            "CREATE TABLE IF NOT EXISTS " + TripContract.NoteTable.TABLE_NAME + " (" +
                    TripContract.NoteTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TripContract.NoteTable.COLUMN_NAME_TEXT + " TEXT," +
                    TripContract.NoteTable.COLUMN_NAME_TITLE + " TEXT)";


    private static final String SQL_DELETE_TRIPS =
            "DROP TABLE IF EXISTS " + TripContract.TripTable.TABLE_NAME;

    private static final String SQL_DELETE_LOCATIONS =
            "DROP TABLE IF EXISTS " + TripContract.LocationTable.TABLE_NAME;

    private static final String SQL_DELETE_NOTES =
            "DROP TABLE IF EXISTS " + TripContract.NoteTable.TABLE_NAME;


    public TripDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TRIPS);
        db.execSQL(SQL_CREATE_LOCATIONS);
        db.execSQL(SQL_CREATE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TRIPS);
        db.execSQL(SQL_DELETE_LOCATIONS);
        db.execSQL(SQL_DELETE_NOTES);
        onCreate(db);
    }

    ArrayList<Trip> listTrips() {
        String sql = "select * from " + TripContract.TripTable.TABLE_NAME;
        String loc_sql = "select * from " + TripContract.LocationTable.TABLE_NAME + " where " + TripContract.LocationTable.COLUMN_NAME_TRIP_ID + " = ";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Trip> storeTrips = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                long trip_id = cursor.getLong(cursor.getColumnIndexOrThrow(TripContract.TripTable._ID));
                String mainLocation = cursor.getString(cursor.getColumnIndexOrThrow(TripContract.TripTable.COLUMN_NAME_MAIN_LOCATION));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow(TripContract.TripTable.COLUMN_NAME_START_DATE));
                Cursor loc_cursor = db.rawQuery(loc_sql+ Long.toString(trip_id),null);
                List<Location> loc_list = new ArrayList<>();

                if (loc_cursor.moveToFirst()) {
                    do {
                        long loc_id = loc_cursor.getLong(loc_cursor.getColumnIndexOrThrow(TripContract.LocationTable._ID));
                        String name = loc_cursor.getString(loc_cursor.getColumnIndexOrThrow(TripContract.LocationTable.COLUMN_NAME_NAME));
                        String time = loc_cursor.getString(loc_cursor.getColumnIndexOrThrow(TripContract.LocationTable.COLUMN_NAME_TIME));
                       loc_list.add(new Location(trip_id,loc_id,name,time));
                    }
                    while (loc_cursor.moveToNext());
                }
                loc_cursor.close();


                storeTrips.add(new Trip(trip_id,mainLocation,startDate,loc_list));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeTrips;
    }

    ArrayList<Note> listNotes(){
        String sql = "select * from " + TripContract.NoteTable.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Note> storeNotes = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                long note_id = cursor.getLong(cursor.getColumnIndexOrThrow(TripContract.NoteTable._ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(TripContract.NoteTable.COLUMN_NAME_TITLE));
                String text = cursor.getString(cursor.getColumnIndexOrThrow(TripContract.NoteTable.COLUMN_NAME_TEXT));
                storeNotes.add(new Note(note_id,text,title));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeNotes;
    }

    long addTrip(Trip trip) {
        ContentValues values = new ContentValues();
        values.put(TripContract.TripTable.COLUMN_NAME_MAIN_LOCATION, trip.getMainLocation());
        values.put(TripContract.TripTable.COLUMN_NAME_START_DATE, trip.getStartDate());


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor dup_check = db.rawQuery("SELECT * FROM "+ TripContract.TripTable.TABLE_NAME+" WHERE "+ TripContract.TripTable.COLUMN_NAME_MAIN_LOCATION+" = '"+trip.getMainLocation()+"' AND "+ TripContract.TripTable.COLUMN_NAME_START_DATE+" = '"+trip.getStartDate()+"'",null);
        if(dup_check.getCount()>0)
        { return -1;}


        db = this.getWritableDatabase();

        long trip_id =  db.insert(TripContract.TripTable.TABLE_NAME, null, values);

        for (Location loc:trip.getLocations()
             ) {
            ContentValues loc_values = new ContentValues();
            loc_values.put(TripContract.LocationTable.COLUMN_NAME_TRIP_ID, trip_id);
            loc_values.put(TripContract.LocationTable.COLUMN_NAME_NAME, loc.getName());
            loc_values.put(TripContract.LocationTable.COLUMN_NAME_TIME,loc.getTime());

            long loc_id =  db.insert(TripContract.LocationTable.TABLE_NAME, null, loc_values);
        }
        return trip_id;
    }

    long addNote(Note note){
        ContentValues values = new ContentValues();
        values.put(TripContract.NoteTable.COLUMN_NAME_TEXT, note.getNoteText());
        values.put(TripContract.NoteTable.COLUMN_NAME_TITLE, note.getNoteTitle());


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor dup_check = db.rawQuery("SELECT * FROM "+ TripContract.NoteTable.TABLE_NAME+" WHERE "+ TripContract.NoteTable.COLUMN_NAME_TITLE+" = '"+note.getNoteTitle()+"'",null);
        if(dup_check.getCount()>0)
        { return -1;}


        db = this.getWritableDatabase();

        long note_id =  db.insert(TripContract.NoteTable.TABLE_NAME, null, values);


        return note_id;
    }
    void updateTrip(Trip trip,boolean location_changed) {
        ContentValues values = new ContentValues();
        values.put(TripContract.TripTable.COLUMN_NAME_MAIN_LOCATION, trip.getMainLocation());
        values.put(TripContract.TripTable.COLUMN_NAME_START_DATE, trip.getStartDate());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TripContract.TripTable.TABLE_NAME, values, TripContract.TripTable._ID + " = ?", new String[]{Long.toString(trip.getTripId())});

        if(location_changed){
            db.delete(TripContract.LocationTable.TABLE_NAME, TripContract.LocationTable.COLUMN_NAME_TRIP_ID + " = ?",new String[]{Long.toString(trip.getTripId())});
            for (Location loc:trip.getLocations()
            ) {
                ContentValues loc_values = new ContentValues();
                loc_values.put(TripContract.LocationTable.COLUMN_NAME_TRIP_ID, trip.getTripId());
                loc_values.put(TripContract.LocationTable.COLUMN_NAME_NAME, loc.getName());
                loc_values.put(TripContract.LocationTable.COLUMN_NAME_TIME,loc.getTime());
                long loc_id =  db.insert(TripContract.LocationTable.TABLE_NAME, null, loc_values);
        }

    }

    }

    void updateNote(Note note){
        ContentValues values = new ContentValues();
        values.put(TripContract.NoteTable.COLUMN_NAME_TEXT, note.getNoteText());
        values.put(TripContract.NoteTable.COLUMN_NAME_TITLE,note.getNoteTitle());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TripContract.NoteTable.TABLE_NAME, values, TripContract.NoteTable._ID + " = ?", new String[]{Long.toString(note.getNoteId())});
    }
    void deleteTrip(long trip_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TripContract.TripTable.TABLE_NAME, TripContract.TripTable._ID + " = ?", new String[]{Long.toString(trip_id)});
        db.delete(TripContract.LocationTable.TABLE_NAME, TripContract.LocationTable.COLUMN_NAME_TRIP_ID + " = ?",new String[]{Long.toString(trip_id)});
    }

    void deleteNote(long note_id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TripContract.NoteTable.TABLE_NAME, TripContract.NoteTable._ID + " = ?", new String[]{Long.toString(note_id)});
    }

    long getTripId(Trip trip){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor match = db.rawQuery("SELECT * FROM "+ TripContract.TripTable.TABLE_NAME+" WHERE "+ TripContract.TripTable.COLUMN_NAME_MAIN_LOCATION+" = '"+trip.getMainLocation()+"' AND "+ TripContract.TripTable.COLUMN_NAME_START_DATE+" = '"+trip.getStartDate()+"'",null);
        if(match.moveToFirst()){
                return match.getLong(match.getColumnIndexOrThrow(TripContract.TripTable._ID));
        }
        else
            return -1;
    }

    long getNoteId(Note note){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor match = db.rawQuery("SELECT * FROM "+ TripContract.NoteTable.TABLE_NAME+" WHERE "+ TripContract.NoteTable.COLUMN_NAME_TITLE+" = '"+note.getNoteTitle()+"'",null);
        if(match.moveToFirst()){
            return match.getLong(match.getColumnIndexOrThrow(TripContract.NoteTable._ID));
        }
        else
            return -1;
    }
}

