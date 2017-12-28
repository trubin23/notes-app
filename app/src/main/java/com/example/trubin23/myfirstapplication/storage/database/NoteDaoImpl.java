package com.example.trubin23.myfirstapplication.storage.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.trubin23.myfirstapplication.storage.model.NoteStorage;

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
    public NoteStorage getNote(@NonNull final String uid) {
        NoteStorage noteStorage = null;
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

                noteStorage = new NoteStorage(uid, title, content, color, destroyDate);
            }
            cursor.close();

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "public NoteView getNote(long id)", e);
        } finally {
            db.endTransaction();
        }

        return noteStorage;
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
            Log.e(TAG, "public List<NoteView> getAllNote()", e);
        } finally {
            db.endTransaction();
        }

        return cursor;
    }

    @NonNull
    @Override
    public List<NoteStorage> getAllNote() {
        List<NoteStorage> noteStorages = new ArrayList<>();

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

                NoteStorage noteStorage = new NoteStorage(uid, title, content, color, destroyDate);

                noteStorages.add(noteStorage);
            }
            cursor.close();

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "public List<NoteView> getAllNote()", e);
        } finally {
            db.endTransaction();
        }

        return noteStorages;
    }

    @Override
    public void notesSync(@NonNull List<NoteStorage> noteStorages) {
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_NOTE, null, null);

            for (NoteStorage noteStorage : noteStorages){
                ContentValues values = new ContentValues();
                values.put(COLUMN_NOTE_UID, noteStorage.getUid());
                values.put(COLUMN_NOTE_TITLE, noteStorage.getTitle());
                values.put(COLUMN_NOTE_CONTENT, noteStorage.getContent());
                values.put(COLUMN_NOTE_COLOR, noteStorage.getColor());
                values.put(COLUMN_NOTE_DESTROY_DATE, noteStorage.getDestroyDate());

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
    public void addNote(@NonNull final NoteStorage noteStorage) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_UID, noteStorage.getUid());
        values.put(COLUMN_NOTE_TITLE, noteStorage.getTitle());
        values.put(COLUMN_NOTE_CONTENT, noteStorage.getContent());
        values.put(COLUMN_NOTE_COLOR, noteStorage.getColor());
        values.put(COLUMN_NOTE_DESTROY_DATE, noteStorage.getDestroyDate());

        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            long id = db.insert(TABLE_NOTE, null, values);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "public void addNote(@NonNull final NoteView noteStorage)", e);
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
    public void updateNote(@NonNull final NoteStorage noteStorage) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, noteStorage.getTitle());
        values.put(COLUMN_NOTE_CONTENT, noteStorage.getContent());
        values.put(COLUMN_NOTE_COLOR, noteStorage.getColor());
        values.put(COLUMN_NOTE_DESTROY_DATE, noteStorage.getDestroyDate());

        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.update(TABLE_NOTE, values, COLUMN_NOTE_UID + " = ?", new String[]{ noteStorage.getUid()});

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "public void updateNote(@NonNull final NoteView noteStorage)", e);
        } finally {
            db.endTransaction();
        }
    }
}
