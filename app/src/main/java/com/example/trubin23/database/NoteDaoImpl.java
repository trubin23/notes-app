package com.example.trubin23.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.trubin23.myfirstapplication.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trubin23 on 11.12.17.
 */

public class NoteDaoImpl implements NoteDao {

    static String NOTE_CREATE_TABLE = "CREATE TABLE " + TABLE_NOTE + "("
            + COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NOTE_TITLE + " TEXT,"
            + COLUMN_NOTE_TEXT + " TEXT,"
            + COLUMN_NOTE_DATE + " TEXT)";

    private static final String QUERY_NOTE = "SELECT * FROM " + TABLE_NOTE
            + " WHERE " + COLUMN_NOTE_ID + " = ?";

    private DatabaseHelper mDbOpenHelper;

    public NoteDaoImpl(DatabaseHelper dbOpenHelper) {
        mDbOpenHelper = dbOpenHelper;
    }

    @Nullable
    @Override
    public Note getNote(long id) {
        Note note = null;
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY_NOTE, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TITLE));
            String text = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TEXT));
            String date = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_DATE));

            note = new Note(id, title, text, date);
        }
        cursor.close();
        db.close();

        return note;
    }

    @NonNull
    @Override
    public List<Note> getAllNote() {
        List<Note> notes = new ArrayList<>();

        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTE,
                COLUMNS, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_ID)));
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TITLE));
                String text = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TEXT));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_DATE));

                Note note = new Note(id, title, text, date);

                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return notes;
    }

    @Override
    public void addNote(@NonNull final Note note) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, note.getTitle());
        values.put(COLUMN_NOTE_TEXT, note.getText());
        values.put(COLUMN_NOTE_DATE, note.getDate());

        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

        db.insert(TABLE_NOTE, null, values);

        db.close();
    }

    @Override
    public void deleteNote(final long id) {
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

        db.delete(TABLE_NOTE, COLUMN_NOTE_ID + " = ?",
                new String[]{String.valueOf(id)});

        db.close();
    }

    @Override
    public void updateNote(@NonNull final Note note) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, note.getTitle());
        values.put(COLUMN_NOTE_TEXT, note.getText());
        values.put(COLUMN_NOTE_DATE, note.getDate());

        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

        db.update(TABLE_NOTE, values, COLUMN_NOTE_ID + " = ?",
                new String[]{String.valueOf(note.getId())});

        db.close();
    }
}
