package com.example.trubin23.database;

import android.support.annotation.NonNull;

import com.example.trubin23.myfirstapplication.Note;

import java.util.List;

/**
 * Created by trubin23 on 11.12.17.
 */

public interface NoteDao {
    static final String TABLE_NOTE = "note";

    static final String COLUMN_NOTE_ID = "note_id";
    static final String COLUMN_NOTE_TITLE = "note_title";
    static final String COLUMN_NOTE_TEXT = "note_text";
    static final String COLUMN_NOTE_DATE = "note_date";

    static final String[] COLUMNS = {
            COLUMN_NOTE_ID,
            COLUMN_NOTE_TITLE,
            COLUMN_NOTE_TEXT,
            COLUMN_NOTE_DATE
    };

    static final String NOTE_CREATE_TABLE = "CREATE TABLE " + TABLE_NOTE + "("
            + COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NOTE_TITLE + " TEXT,"
            + COLUMN_NOTE_TEXT + " TEXT,"
            + COLUMN_NOTE_DATE + " TEXT)";

    @NonNull
    public List<Note> getAllNote();

    public void addNote(@NonNull final Note note);

    public void deleteNote(final long id);

    public void updateNote(@NonNull final Note note);
}
