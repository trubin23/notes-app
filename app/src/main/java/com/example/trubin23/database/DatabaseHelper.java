package com.example.trubin23.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import static com.example.trubin23.database.NoteDao.TABLE_NOTE;
import static com.example.trubin23.database.NoteDao.COLUMN_NOTE_ID;
import static com.example.trubin23.database.NoteDao.COLUMN_NOTE_TITLE;
import static com.example.trubin23.database.NoteDao.COLUMN_NOTE_TEXT;
import static com.example.trubin23.database.NoteDao.COLUMN_NOTE_DATE;

/**
 * Created by trubin23 on 07.12.17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DB_NAME = "Notes.db";
    private static final int DB_VERSION = 1;

    public static final long DEFAULT_ID = -1;

    DatabaseHelper(@NonNull Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        final String NOTE_CREATE_TABLE = "CREATE TABLE " + TABLE_NOTE + "("
                + COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOTE_TITLE + " TEXT,"
                + COLUMN_NOTE_TEXT + " TEXT,"
                + COLUMN_NOTE_DATE + " TEXT)";

        db.beginTransaction();
        try {
            db.execSQL(NOTE_CREATE_TABLE);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "public void onCreate(SQLiteDatabase db)", e);
        } finally {
            db.endTransaction();
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}