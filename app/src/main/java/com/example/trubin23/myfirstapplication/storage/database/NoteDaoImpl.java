package com.example.trubin23.myfirstapplication.storage.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trubin23 on 11.12.17.
 */

public class NoteDaoImpl implements NoteDao {

    private static final String TAG = "NoteDaoImpl";

    static final String NOTE_CREATE_TABLE = "CREATE TABLE " + TABLE_NOTE + "("
            + COLUMN_NOTE_UID + " TEXT PRIMARY KEY,"
            + COLUMN_NOTE_TITLE + " TEXT,"
            + COLUMN_NOTE_CONTENT + " TEXT,"
            + COLUMN_NOTE_COLOR + " TEXT,"
            + COLUMN_NOTE_DESTROY_DATE + " INTEGER)";

    private static final String QUERY_NOTE = "SELECT * FROM " + TABLE_NOTE
            + " WHERE " + COLUMN_NOTE_UID + " = ?";

    private DatabaseHelper mDbOpenHelper;

    public NoteDaoImpl(@NonNull DatabaseHelper dbOpenHelper) {
        mDbOpenHelper = dbOpenHelper;
    }

    @Nullable
    @Override
    public Note getNote(@NonNull final String uid) {
        Note note = null;
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Cursor cursor = db.rawQuery(QUERY_NOTE, new String[]{uid});

            if (cursor.moveToFirst()) {
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TITLE));
                String content = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_CONTENT));
                String color = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_COLOR));
                Integer destroyDate = Integer.parseInt(cursor.getString(
                        cursor.getColumnIndex(COLUMN_NOTE_DESTROY_DATE)));

                note = new Note(uid, title, content, color, destroyDate);
            }
            cursor.close();

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "public Note getNote(long id)", e);
        } finally {
            db.endTransaction();
        }

        return note;
    }

    @Nullable
    @Override
    public Cursor getCursorAllData() {
        Cursor cursor = null;
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            cursor = db.query(TABLE_NOTE,
                    COLUMNS, null, null, null, null, null);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "public List<Note> getAllNote()", e);
        } finally {
            db.endTransaction();
        }

        return cursor;
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

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String uid = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_UID));
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TITLE));
                String content = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_CONTENT));
                String color = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_COLOR));
                Integer destroyDate = Integer.parseInt(cursor.getString(
                        cursor.getColumnIndex(COLUMN_NOTE_DESTROY_DATE)));

                Note note = new Note(uid, title, content, color, destroyDate);

                notes.add(note);
            }
            cursor.close();

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "public List<Note> getAllNote()", e);
        } finally {
            db.endTransaction();
        }

        return notes;
    }

    @Override
    public void notesSync(@NonNull List<Note> notes) {
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_NOTE, null, null);

            for (Note note : notes){
                ContentValues values = new ContentValues();
                values.put(COLUMN_NOTE_UID, note.getUid());
                values.put(COLUMN_NOTE_TITLE, note.getTitle());
                values.put(COLUMN_NOTE_CONTENT, note.getContent());
                values.put(COLUMN_NOTE_COLOR, note.getColor());
                values.put(COLUMN_NOTE_DESTROY_DATE, note.getDestroyDate());

                db.insert(TABLE_NOTE, null, values);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "public void deleteNote(@NonNull final String uid)", e);
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void addNote(@NonNull final Note note) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_UID, note.getUid());
        values.put(COLUMN_NOTE_TITLE, note.getTitle());
        values.put(COLUMN_NOTE_CONTENT, note.getContent());
        values.put(COLUMN_NOTE_COLOR, note.getColor());
        values.put(COLUMN_NOTE_DESTROY_DATE, note.getDestroyDate());

        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.insert(TABLE_NOTE, null, values);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "public void addNote(@NonNull final Note note)", e);
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void deleteNote(@NonNull final String uid) {
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_NOTE, COLUMN_NOTE_UID + " = ?", new String[]{uid});

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "public void deleteNote(@NonNull final String uid)", e);
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void updateNote(@NonNull final Note note) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, note.getTitle());
        values.put(COLUMN_NOTE_CONTENT, note.getContent());
        values.put(COLUMN_NOTE_COLOR, note.getColor());
        values.put(COLUMN_NOTE_DESTROY_DATE, note.getDestroyDate());

        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.update(TABLE_NOTE, values, COLUMN_NOTE_UID + " = ?", new String[]{note.getUid()});

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "public void updateNote(@NonNull final Note note)", e);
        } finally {
            db.endTransaction();
        }
    }
}
