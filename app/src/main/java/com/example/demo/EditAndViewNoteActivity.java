package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditAndViewNoteActivity extends AppCompatActivity {
    EditText text,title;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_and_view_note);

        text = findViewById(R.id.editNoteText);
        title = findViewById(R.id.editNoteTitle);

        back = findViewById(R.id.backToNotesBtn);

        text.setFocusable(false);
        title.setFocusable(false);

        text.setText(getIntent().getStringExtra("Text"));
        title.setText(getIntent().getStringExtra("Title"));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditAndViewNoteActivity.this,ViewAllNotesActivity.class));
            }
        });

    }
}