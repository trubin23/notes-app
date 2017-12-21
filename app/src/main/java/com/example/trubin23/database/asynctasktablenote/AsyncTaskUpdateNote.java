package com.example.trubin23.database.asynctasktablenote;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.example.trubin23.database.NoteDao;
import com.example.trubin23.myfirstapplication.MainActivity;
import com.example.trubin23.myfirstapplication.Note;

/**
 * Created by trubin23 on 15.12.17.
 */

public class AsyncTaskUpdateNote extends AsyncTaskBase {

    private Note mNote;

    public AsyncTaskUpdateNote(@NonNull LocalBroadcastManager broadcastManager,
                               @NonNull NoteDao noteDao, @NonNull Note note) {
        super(broadcastManager, noteDao);
        mNote = note;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        mNoteDao.updateNote(mNote);

        Intent intent = new Intent(MainActivity.ACTION_CHANGED_DB);
        mBroadcastManager.sendBroadcast(intent);

        return null;
    }
}
