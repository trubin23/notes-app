package com.example.trubin23.myfirstapplication.storage.database;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.trubin23.myfirstapplication.storage.model.Note;

import java.util.List;

/**
 * Created by trubin23 on 11.12.17.
 */

public interface NoteDao {
    String TABLE_NOTE = "note";

    String COLUMN_NOTE_UID = "note_uid";
    String COLUMN_NOTE_TITLE = "note_title";
    String COLUMN_NOTE_CONTENT = "note_content";
    String COLUMN_NOTE_COLOR = "note_color";
    String COLUMN_NOTE_DESTROY_DATE = "note_destroy_date";

    String[] COLUMNS = {
            COLUMN_NOTE_UID,
            COLUMN_NOTE_TITLE,
            COLUMN_NOTE_CONTENT,
            COLUMN_NOTE_COLOR,
            COLUMN_NOTE_DESTROY_DATE
    };

    @Nullable
    Note getNote(@NonNull final String uid);

    @Nullable
    Cursor getCursorAllData();

    @NonNull
    List<Note> getAllNote();

    void notesSync(@NonNull final List<Note> notes);

    void addNote(@NonNull final Note note);

    void deleteNote(@NonNull final String uid);

    void updateNote(@NonNull final Note note);
}
