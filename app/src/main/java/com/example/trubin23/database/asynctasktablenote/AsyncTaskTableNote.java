package com.example.trubin23.database.asynctasktablenote;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import com.example.trubin23.database.NoteDao;

/**
 * Created by trubin23 on 15.12.17.
 */

abstract class AsyncTaskTableNote extends AsyncTask<Void, Void, Void> {

    LocalBroadcastManager mBroadcastManager;
    NoteDao mNoteDao;

    AsyncTaskTableNote(@NonNull LocalBroadcastManager broadcastManager,
                       @NonNull NoteDao noteDao){
        mBroadcastManager = broadcastManager;
        mNoteDao = noteDao;
    }
}
