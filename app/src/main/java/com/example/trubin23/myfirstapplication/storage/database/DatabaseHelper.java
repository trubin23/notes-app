package com.example.trubin23.myfirstapplication.storage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by trubin23 on 07.12.17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DB_NAME = "Notes.db";
    private static final int DB_VERSION = 1;

    public static final String DEFAULT_ID = "00000000-0000-0000-0000-000000000000";

    private static DatabaseHelper mDatabaseHelper;

    private DatabaseHelper(@NonNull Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @NonNull
    public static DatabaseHelper create(@NonNull Context context) {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = new DatabaseHelper(context);
            return mDatabaseHelper;
        }
        return mDatabaseHelper;
    }

    @Nullable
    public static DatabaseHelper getInstance() {
        return mDatabaseHelper;
    }

    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(NoteDaoImpl.NOTE_CREATE_TABLE);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "create table " + NoteDao.TABLE_NOTE, e);
        } finally {
            db.endTransaction();
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}