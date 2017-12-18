package com.example.trubin23.database.asynctasktablenote;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import com.example.trubin23.database.NoteDao;
import com.example.trubin23.myfirstapplication.EditNoteActivity;
import com.example.trubin23.myfirstapplication.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 18.12.2017.
 */

public class AsyncTaskGetNote extends AsyncTaskTableNote {

	private long mId;

	public AsyncTaskGetNote(@NonNull LocalBroadcastManager broadcastManager,
	                        @NonNull NoteDao noteDao, long id) {
		super(broadcastManager, noteDao);
		mId = id;
	}

	@Override
	protected Void doInBackground(Void... voids) {
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Note note = mNoteDao.getNote(mId);
		List<Note> notes = new ArrayList<>();
		notes.add(note);

		Intent intent = new Intent(EditNoteActivity.ACTION_REFRESH_NOTE);
		intent.putParcelableArrayListExtra(EditNoteActivity.NOTE, new ArrayList<>(notes));
		mBroadcastManager.sendBroadcast(intent);

		return null;
	}
}
