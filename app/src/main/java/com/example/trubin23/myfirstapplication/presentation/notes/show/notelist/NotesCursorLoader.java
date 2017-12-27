package com.example.trubin23.myfirstapplication.presentation.notes.show.notelist;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import android.support.v4.content.CursorLoader;

import com.example.trubin23.myfirstapplication.domain.MyCustomApplication;
import com.example.trubin23.myfirstapplication.storage.database.NoteDao;

/**
 * Created by trubin23 on 23.12.17.
 */

public class NotesCursorLoader extends CursorLoader {

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
