package com.example.trubin23.database;

import android.support.annotation.NonNull;

import com.example.trubin23.myfirstapplication.Note;

import java.util.List;

/**
 * Created by trubin23 on 11.12.17.
 */

public interface NoteDao {
    final String TABLE_NOTE = "note";

    final String COLUMN_NOTE_ID = "note_id";
    final String COLUMN_NOTE_TITLE = "note_title";
    final String COLUMN_NOTE_TEXT = "note_text";
    final String COLUMN_NOTE_DATE = "note_date";

    final String[] COLUMNS = {
            COLUMN_NOTE_ID,
            COLUMN_NOTE_TITLE,
            COLUMN_NOTE_TEXT,
            COLUMN_NOTE_DATE
    };

    final String NOTE_CREATE_TABLE = "CREATE TABLE " + TABLE_NOTE + "("
            + COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NOTE_TITLE + " TEXT,"
            + COLUMN_NOTE_TEXT + " TEXT,"
            + COLUMN_NOTE_DATE + " TEXT)";

    @NonNull
    List<Note> getAllNote();

    void addNote(@NonNull final Note note);

    void deleteNote(final long id);

    void updateNote(@NonNull final Note note);
}
