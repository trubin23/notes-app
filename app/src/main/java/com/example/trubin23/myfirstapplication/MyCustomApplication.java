package com.example.trubin23.myfirstapplication;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.trubin23.database.NoteDao;
import com.example.trubin23.database.NoteDaoImpl;
import com.example.trubin23.json.InitializeData;

import java.util.List;

/**
 * Created by trubin23 on 11.12.2017.
 */

public class MyCustomApplication extends Application {

	private static final String FIRST_RUN = "first_run";
	private NoteDao mNoteDao;

	@Override
	public void onCreate() {
		super.onCreate();

		mNoteDao = new NoteDaoImpl(this);

		SharedPreferences preferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
		if (preferences.getBoolean(FIRST_RUN, true)) {
			List<Note> notes = InitializeData.initializeData(this);
			for (Note note : notes) {
				mNoteDao.addNote(note);
			}
			preferences.edit().putBoolean(FIRST_RUN, false).apply();
		}
	}

	NoteDao getDbNotes(){
		return mNoteDao;
	}
}
