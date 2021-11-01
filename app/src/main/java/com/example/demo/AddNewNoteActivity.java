package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewNoteActivity extends AppCompatActivity {
    EditText text,title;
    Button save;
    TripDbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);
        title = findViewById(R.id.addNoteTitle);
        text = findViewById(R.id.addNoteText);
        db = new TripDbHelper(this);

        save = findViewById(R.id.addNoteSave);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.addNote(new Note(0,text.getText().toString(),title.getText().toString()))==-1){
                    Toast.makeText(AddNewNoteActivity.this,"Note already exists",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AddNewNoteActivity.this,"Note added",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddNewNoteActivity.this,MainActivity.class));
                }
            }
        });

    }
}