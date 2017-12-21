package com.example.trubin23.database.asynctasktablenote;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.example.trubin23.database.NoteDao;
import com.example.trubin23.myfirstapplication.MainActivity;

/**
 * Created by trubin23 on 15.12.17.
 */

public class AsyncTaskDeleteNote extends AsyncTaskBase {

    private String mUid;

    public AsyncTaskDeleteNote(@NonNull LocalBroadcastManager broadcastManager,
                               @NonNull NoteDao noteDao, @NonNull String id) {
        super(broadcastManager, noteDao);
        mUid = id;
    }

    @NonNull
    @Override
    protected Void doInBackground(Void... voids) {
        mNoteDao.deleteNote(mUid);

        Intent intent = new Intent(MainActivity.ACTION_CHANGED_DB);
        mBroadcastManager.sendBroadcast(intent);

        return null;
    }
}
