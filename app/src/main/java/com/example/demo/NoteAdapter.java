package com.example.demo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {
    private TripDbHelper db;
    private List<Note> noteList;
    private Context context;
    private AdapterCallbacks adapterCallBack;

    public NoteAdapter(Context context, List<Note> noteList, TripDbHelper db) {
        this.db = db;
        this.noteList = noteList;
        this.context = context;
        this.adapterCallBack = (AdapterCallbacks)context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_recycle_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.noteTitle.setText(note.getNoteTitle());
        holder.noteDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,EditAndViewNoteActivity.class);
                i.putExtra("Title",note.getNoteTitle());
                i.putExtra("Text",note.getNoteText());
                ((ViewAllNotesActivity)context).startActivity(i);
            }
        });
        holder.noteDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteNote(note.getNoteId());
                noteList.remove(position);
                NoteAdapter.this.notifyItemRemoved(position);
                NoteAdapter.this.notifyItemRangeChanged(position, noteList.size());
                adapterCallBack.checkAdapterIsEmpty(noteList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
