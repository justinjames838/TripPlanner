package com.example.demo;

public class Note {
    public long noteId;
    public String noteText;

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String noteTitle;

    public Note(long noteId, String noteText,String noteTitle) {
        this.noteId = noteId;
        this.noteText = noteText;
        this.noteTitle = noteTitle;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}
