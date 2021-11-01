package com.example.demo;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    CardView parent;
    TextView noteTitle;
    Button noteDetails,noteDelete;
    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        this.parent = itemView.findViewById(R.id.parent);
        this.noteTitle = itemView.findViewById(R.id.note_title);
        this.noteDetails = itemView.findViewById(R.id.note_details_button);
        this.noteDelete = itemView.findViewById(R.id.note_delete);
    }
}
