package com.example.trubin23.myfirstapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import static com.example.trubin23.myfirstapplication.DatabaseHelper.COLUMNS;
import static com.example.trubin23.myfirstapplication.DatabaseHelper.COLUMN_NOTE_DATE;
import static com.example.trubin23.myfirstapplication.DatabaseHelper.COLUMN_NOTE_ID;
import static com.example.trubin23.myfirstapplication.DatabaseHelper.COLUMN_NOTE_TEXT;
import static com.example.trubin23.myfirstapplication.DatabaseHelper.COLUMN_NOTE_TITLE;
import static com.example.trubin23.myfirstapplication.DatabaseHelper.TABLE_NOTE;

/**
 * Created by trubin23 on 11.12.17.
 */

class DatabaseConnector {

    private DatabaseHelper mDBOpenHelper;

    DatabaseConnector(Context context) {
        mDBOpenHelper = new DatabaseHelper(context);
    }

    @NonNull
    List<Note> getAllNote() {
        List<Note> noteList = new ArrayList<>();

        SQLiteDatabase db = mDBOpenHelper.getReadableDatabase();
        db.beginTransaction();// transaction for readable ?
        try {
            Cursor cursor = db.query(TABLE_NOTE,
                    COLUMNS, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_ID)));
                    String title = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TITLE));
                    String text = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TEXT));
                    String date = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_DATE));

                    Note note = new Note(id, title, text, date);

                    noteList.add(note);
                } while (cursor.moveToNext());
            }
            cursor.close();

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        return noteList;
    }

    void addNote(@NonNull final Note note) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();

                db.beginTransaction();
                try {
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_NOTE_TITLE, note.getTitle());
                    values.put(COLUMN_NOTE_TEXT, note.getText());
                    values.put(COLUMN_NOTE_DATE, note.getDate());

                    db.insert(TABLE_NOTE, null, values);

                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                    db.close();
                }
            }
        });
    }

    void deleteNote(final long id) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();

                db.beginTransaction();
                try {
                    db.delete(TABLE_NOTE, COLUMN_NOTE_ID + " = ?",
                            new String[]{String.valueOf(id)});

                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                    db.close();
                }
            }
        });
    }

    void updateNote(@NonNull final Note note) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = mDBOpenHelper.getWritableDatabase();

                db.beginTransaction();
                try {
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_NOTE_TITLE, note.getTitle());
                    values.put(COLUMN_NOTE_TEXT, note.getText());
                    values.put(COLUMN_NOTE_DATE, note.getDate());

                    db.update(TABLE_NOTE, values, COLUMN_NOTE_ID + " = ?",
                            new String[]{String.valueOf(note.getId())});

                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                    db.close();
                }
            }
        });
    }
}
