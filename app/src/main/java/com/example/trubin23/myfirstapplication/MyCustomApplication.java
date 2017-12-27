package com.example.trubin23.myfirstapplication;

import android.app.Application;
import android.support.annotation.NonNull;

import com.example.trubin23.myfirstapplication.storage.database.DatabaseHelper;
import com.example.trubin23.myfirstapplication.storage.database.NoteDao;
import com.example.trubin23.myfirstapplication.storage.database.NoteDaoImpl;

/**
 * Created by trubin23 on 11.12.2017.
 */

public class MyCustomApplication extends Application {

    private NoteDao mNoteDao;

    @Override
    public void onCreate() {
        super.onCreate();

        DatabaseHelper databaseHelper = DatabaseHelper.create(this);

        mNoteDao = new NoteDaoImpl(databaseHelper);
    }

    @NonNull
    public NoteDao getNoteDao() {
        return mNoteDao;
    }
}
