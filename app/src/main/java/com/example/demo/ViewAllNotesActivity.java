package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ViewAllNotesActivity extends AppCompatActivity implements AdapterCallbacks {

    TripDbHelper db;
    RecyclerView note_Recycle;
    TextView no_available_note;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_notes);

        no_available_note = findViewById(R.id.no_available_notes);
        add = findViewById(R.id.addNoteBtn);
        note_Recycle = findViewById(R.id.note_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        note_Recycle.setLayoutManager(linearLayoutManager);

        db = new TripDbHelper(this);
        ArrayList<Note> allNotes = db.listNotes();
//        System.out.println(allTrips.size());

        if (allNotes.size() > 0) {
            note_Recycle.setVisibility(View.VISIBLE);
            no_available_note.setVisibility(View.GONE);
            NoteAdapter nAdapter = new NoteAdapter(this,allNotes,db);
//            tAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//                @Override
//                public void onChanged() {
//                    super.onChanged();
//                    checkAdapterIsEmpty(tAdapter.getItemCount());
//                }
//            });
            note_Recycle.setAdapter(nAdapter);

        }
        else {
            note_Recycle.setVisibility(View.GONE);
            no_available_note.setVisibility(View.VISIBLE);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewAllNotesActivity.this,AddNewNoteActivity.class));
            }
        });
    }


    @Override
    public void checkAdapterIsEmpty(int size) {
        if(size == 0){
            note_Recycle.setVisibility(View.GONE);
            no_available_note.setVisibility(View.VISIBLE);
        }
    }
}