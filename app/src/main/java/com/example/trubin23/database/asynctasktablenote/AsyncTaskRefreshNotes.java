package com.example.trubin23.database.asynctasktablenote;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.example.trubin23.database.NoteDao;
import com.example.trubin23.myfirstapplication.MainActivity;
import com.example.trubin23.myfirstapplication.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trubin23 on 15.12.17.
 */

public class AsyncTaskRefreshNotes extends AsyncTaskBase {

    public AsyncTaskRefreshNotes(@NonNull LocalBroadcastManager broadcastManager,
                                 @NonNull NoteDao noteDao) {
        super(broadcastManager, noteDao);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<Note> notes = mNoteDao.getAllNote();

        Intent intent = new Intent(MainActivity.ACTION_REFRESH_NOTES);
        intent.putParcelableArrayListExtra(MainActivity.NOTES, new ArrayList<>(notes));
        mBroadcastManager.sendBroadcast(intent);

        return null;
    }
}
