package com.example.trubin23.myfirstapplication;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.trubin23.database.NoteDao;

/**
 * Created by trubin23 on 23.12.17.
 */

class NotesCursorLoader extends CursorLoader {

    private NoteDao mNoteDao;

    public NotesCursorLoader(@NonNull Context context) {
        super(context);
        mNoteDao = ((MyCustomApplication) context.getApplicationContext()).getNoteDao();
    }

    @Override
    public Cursor loadInBackground() {
        return mNoteDao.getCursorAllData();
    }
}
