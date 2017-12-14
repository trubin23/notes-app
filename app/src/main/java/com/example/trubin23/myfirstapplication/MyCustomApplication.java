package com.example.trubin23.myfirstapplication;

import android.app.Application;
import android.support.annotation.NonNull;

import com.example.trubin23.database.DatabaseHelper;
import com.example.trubin23.database.NoteDao;
import com.example.trubin23.database.NoteDaoImpl;

/**
 * Created by trubin23 on 11.12.2017.
 */

public class MyCustomApplication extends Application {

	private NoteDao mNoteDao;

	@Override
	public void onCreate() {
		super.onCreate();

		DatabaseHelper databaseHelper = new DatabaseHelper(this);

		mNoteDao = new NoteDaoImpl(databaseHelper);
	}

	@NonNull
	NoteDao getNoteDao(){
		return mNoteDao;
	}
}
