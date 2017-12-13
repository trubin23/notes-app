package com.example.trubin23.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.trubin23.myfirstapplication.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trubin23 on 11.12.17.
 */

public class NoteDaoImpl implements NoteDao {

    private static final String TAG = "NoteDaoImpl";

    private static final String QUERY_NOTE = "SELECT * FROM " + TABLE_NOTE
            + " WHERE " + COLUMN_NOTE_ID + " = ?";

    private DatabaseHelper mDbOpenHelper;

    public NoteDaoImpl(Context context) {
        mDbOpenHelper = new DatabaseHelper(context);
    }

    @Nullable
    @Override
    public Note getNote(long id) {
        Note note = null;
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery(QUERY_NOTE, new String[]{String.valueOf(id)});

            if (cursor.moveToFirst()) {
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TITLE));
                String text = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TEXT));
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_DATE));

                note = new Note(id, title, text, date);
            }
            cursor.close();

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "public List<Note> getAllNote()", e);
        } finally {
            db.endTransaction();
            db.close();
        }

        return note;
    }

    @NonNull
    @Override
    public List<Note> getAllNote() {
        List<Note> notes = new ArrayList<>();

        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
        db.beginTransaction();
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

                    notes.add(note);
                } while (cursor.moveToNext());
            }
            cursor.close();

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "public List<Note> getAllNote()", e);
        } finally {
            db.endTransaction();
            db.close();
        }

        return notes;
    }

    @Override
    public void addNote(@NonNull final Note note) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

                db.beginTransaction();
                try {
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_NOTE_TITLE, note.getTitle());
                    values.put(COLUMN_NOTE_TEXT, note.getText());
                    values.put(COLUMN_NOTE_DATE, note.getDate());

                    db.insert(TABLE_NOTE, null, values);

                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    Log.e(TAG, "public void addNote(@NonNull final Note note)", e);
                } finally {
                    db.endTransaction();
                    db.close();
                }
            }
        });
    }

    @Override
    public void deleteNote(final long id) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

                db.beginTransaction();
                try {
                    db.delete(TABLE_NOTE, COLUMN_NOTE_ID + " = ?",
                            new String[]{String.valueOf(id)});

                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    Log.e(TAG, "public void deleteNote(final long id)", e);
                } finally {
                    db.endTransaction();
                    db.close();
                }
            }
        });
    }

    @Override
    public void updateNote(@NonNull final Note note) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(COLUMN_NOTE_TITLE, note.getTitle());
                values.put(COLUMN_NOTE_TEXT, note.getText());
                values.put(COLUMN_NOTE_DATE, note.getDate());

                db.beginTransaction();
                try {

                    db.update(TABLE_NOTE, values, COLUMN_NOTE_ID + " = ?",
                            new String[]{String.valueOf(note.getId())});

                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    Log.e(TAG, "public void updateNote(@NonNull final Note note)", e);
                } finally {
                    db.endTransaction();
                    db.close();
                }
            }
        });
    }
}
