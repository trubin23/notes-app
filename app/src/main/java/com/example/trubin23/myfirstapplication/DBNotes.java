package com.example.trubin23.myfirstapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.trubin23.json.InitializeData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trubin23 on 07.12.17.
 */

class DBNotes extends SQLiteOpenHelper {

    private static final String DB_NAME = "Notes.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NOTE = "note";

    private static final String COLUMN_NOTE_ID = "note_id";
    private static final String COLUMN_NOTE_TITLE = "note_title";
    private static final String COLUMN_NOTE_TEXT = "note_text";
    private static final String COLUMN_NOTE_DATE = "note_date";

    private static final String[] COLUMNS = {
            COLUMN_NOTE_ID,
            COLUMN_NOTE_TITLE,
            COLUMN_NOTE_TEXT,
            COLUMN_NOTE_DATE
    };

    static final long DEFAULT_ID = -1;

    private final Context mContext;

    DBNotes(@NonNull Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mContext = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NOTE + "("
                + COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOTE_TITLE + " TEXT,"
                + COLUMN_NOTE_TEXT + " TEXT,"
                + COLUMN_NOTE_DATE + " TEXT)");

        List<Note> notes = InitializeData.initializeData(mContext);
        for (Note note : notes) {
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
            }
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @NonNull
    List<Note> getAllNote() {
        List<Note> noteList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
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

    @Nullable
    Note addNote(@NonNull Note noteInsert) {
        SQLiteDatabase db = this.getWritableDatabase();

        long id = DEFAULT_ID; // need '-1' - is redundant ?
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NOTE_TITLE, noteInsert.getTitle());
            values.put(COLUMN_NOTE_TEXT, noteInsert.getText());
            values.put(COLUMN_NOTE_DATE, noteInsert.getDate());

            id = db.insert(TABLE_NOTE, null, values);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        if (id != DEFAULT_ID) {
            return new Note(id, noteInsert.getTitle(), noteInsert.getText(), noteInsert.getDate());
        } else {
            return null;
        }
    }

    boolean deleteNote(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        boolean result = false; // need 'false' - is redundant ?
        db.beginTransaction();
        try {
            int rowsDelete = db.delete(TABLE_NOTE, COLUMN_NOTE_ID + " = ?",
                    new String[]{String.valueOf(id)});

            result = rowsDelete > 0;

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        return result;
    }

    boolean updateNote(@NonNull Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        boolean result = false; // need 'false' - is redundant ?
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NOTE_TITLE, note.getTitle());
            values.put(COLUMN_NOTE_TEXT, note.getText());
            values.put(COLUMN_NOTE_DATE, note.getDate());

            // updating row
            int rowsUpdate = db.update(TABLE_NOTE, values, COLUMN_NOTE_ID + " = ?",
                    new String[]{String.valueOf(note.getId())});

            result = rowsUpdate > 0;

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        return result;
    }
}