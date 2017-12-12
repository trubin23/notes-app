package com.example.trubin23.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import static com.example.trubin23.database.NoteDao.NOTE_CREATE_TABLE;

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