package com.example.trubin23.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.example.trubin23.json.InitializeData;
import com.example.trubin23.myfirstapplication.Note;

import java.util.List;

import static com.example.trubin23.database.NoteDao.*;

/**
 * Created by trubin23 on 07.12.17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Notes.db";
    private static final int DB_VERSION = 1;

    public static final long DEFAULT_ID = -1;

    private final Context mContext;

    DatabaseHelper(@NonNull Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mContext = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NOTE_CREATE_TABLE);

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