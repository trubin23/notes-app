package com.example.trubin23.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.trubin23.myfirstapplication.Note;

import java.util.List;

/**
 * Created by trubin23 on 11.12.17.
 */

public interface NoteDao {
    String TABLE_NOTE = "note";

    String COLUMN_NOTE_ID = "note_id";
    String COLUMN_NOTE_TITLE = "note_title";
    String COLUMN_NOTE_TEXT = "note_text";
    String COLUMN_NOTE_DATE = "note_date";

    String[] COLUMNS = {
            COLUMN_NOTE_ID,
            COLUMN_NOTE_TITLE,
            COLUMN_NOTE_TEXT,
            COLUMN_NOTE_DATE
    };

    @Nullable
    Note getNote(final long id);

    @NonNull
    List<Note> getAllNote();

    void addNote(@NonNull final Note note);

    void deleteNote(final long id);

    void updateNote(@NonNull final Note note);
}
