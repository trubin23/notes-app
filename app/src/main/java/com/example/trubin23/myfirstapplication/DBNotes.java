package com.example.trubin23.myfirstapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    private static final String LOG = "DBNotes";

    private final Context context;

    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    DBNotes(@NonNull Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NOTE + "("
                + COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOTE_TITLE + " TEXT,"
                + COLUMN_NOTE_TEXT + " TEXT,"
                + COLUMN_NOTE_DATE + " DATE)");

        List<Note> notes = initializeData();
        for (Note note : notes){
            addNote(db, note);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @NonNull
    private List<Note> initializeData() {
        String json = "";

        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.notes);

            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            json = new String(b);
        } catch (IOException e) {
            Log.e(LOG, "List<Note> initializeData()", e);
        }

        Moshi moshi = new Moshi.Builder().build();

        Type listOfCardsType = Types.newParameterizedType(List.class, NoteParse.class);
        JsonAdapter<List<NoteParse>> jsonAdapter = moshi.adapter(listOfCardsType);

        List<Note> notes = new ArrayList<>();
        try {
            List<NoteParse> notesParse = jsonAdapter.fromJson(json);
            if (notesParse != null) {
                for (NoteParse noteParse : notesParse) {
                    notes.add(new Note(noteParse.title, noteParse.text, noteParse.date));
                }
            }
        } catch (IOException e) {
            Log.e(LOG, "List<Note> initializeData()", e);
        }

        return notes;
    }

    @NonNull
    List<Note> getAllNote() {
        String[] columns = {
                COLUMN_NOTE_ID,
                COLUMN_NOTE_TITLE,
                COLUMN_NOTE_TEXT,
                COLUMN_NOTE_DATE
        };

        List<Note> noteList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTE,
                columns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_ID)));
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TITLE));
                String text = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_TEXT));
                String dateString = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE_DATE));

                Date date;
                try {
                    date = mDateFormat.parse(dateString);
                } catch (ParseException e) {
                    date = new Date(0);
                }

                Note note = new Note(id, title, text, date);

                noteList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return noteList;
    }

    @Nullable
    Note addNote(Note noteInsert){
        SQLiteDatabase db = this.getWritableDatabase();

        Note note = addNote(db, noteInsert);

        db.close();

        return note;
    }

    @Nullable
    private Note addNote(@NonNull SQLiteDatabase db, @NonNull Note noteInsert){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, noteInsert.getTitle());
        values.put(COLUMN_NOTE_TEXT, noteInsert.getText());
        values.put(COLUMN_NOTE_DATE, mDateFormat.format(noteInsert.getDate()));

        // Inserting Row
        long id = db.insert(TABLE_NOTE, null, values);

        if (id != Note.DEFAULT_ID){
            return new Note(id, noteInsert.getTitle(), noteInsert.getText(), noteInsert.getDate());
        } else {
            return null;
        }
    }

    boolean deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        int rowsDelete = db.delete(TABLE_NOTE, COLUMN_NOTE_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();

        if (rowsDelete!=0){
            return true;
        }else{
            return false;
        }
    }

    boolean updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, note.getTitle());
        values.put(COLUMN_NOTE_TEXT, note.getText());
        values.put(COLUMN_NOTE_DATE, mDateFormat.format(note.getDate()));

        // updating row
        int rowsUpdate = db.update(TABLE_NOTE, values, COLUMN_NOTE_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();

        if (rowsUpdate!=0){
            return true;
        }else{
            return false;
        }
    }
}