package com.example.trubin23.myfirstapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.example.trubin23.json.InitializeData;

import java.util.List;

/**
 * Created by trubin23 on 07.12.17.
 */

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Notes.db";
    private static final int DB_VERSION = 1;

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

    static final long DEFAULT_ID = -1;

    private final Context mContext;

    DatabaseHelper(@NonNull Context context) {
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
}