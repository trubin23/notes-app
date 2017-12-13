package com.example.trubin23.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.trubin23.json.InitializeData;
import com.example.trubin23.myfirstapplication.Note;

import java.util.List;

import static com.example.trubin23.database.NoteDao.COLUMN_NOTE_DATE;
import static com.example.trubin23.database.NoteDao.COLUMN_NOTE_TEXT;
import static com.example.trubin23.database.NoteDao.COLUMN_NOTE_TITLE;
import static com.example.trubin23.database.NoteDao.TABLE_NOTE;
import static com.example.trubin23.database.NoteDaoImpl.NOTE_CREATE_TABLE;

/**
 * Created by trubin23 on 07.12.17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DB_NAME = "Notes.db";
    private static final int DB_VERSION = 1;

    public static final long DEFAULT_ID = -1;

    private Context mContext;

    public DatabaseHelper(@NonNull Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(NOTE_CREATE_TABLE);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "create table", e);
        } finally {
            db.endTransaction();
        }

        List<Note> notes = InitializeData.initializeData(mContext);
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (Note note : notes) {
                values.put(COLUMN_NOTE_TITLE, note.getTitle());
                values.put(COLUMN_NOTE_TEXT, note.getText());
                values.put(COLUMN_NOTE_DATE, note.getDate());
                db.insert(TABLE_NOTE, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "create table", e);
        } finally {
            db.endTransaction();
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}