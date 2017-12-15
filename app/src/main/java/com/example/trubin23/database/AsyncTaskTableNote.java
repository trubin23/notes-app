package com.example.trubin23.database;

import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by trubin23 on 15.12.17.
 */

public abstract class AsyncTaskTableNote extends AsyncTask<Void, Void, Void> {

    protected LocalBroadcastManager mBroadcastManager;
    protected NoteDao mNoteDao;

    AsyncTaskTableNote(LocalBroadcastManager broadcastManager, NoteDao noteDao){
        mBroadcastManager = broadcastManager;
        mNoteDao = noteDao;
    }
}
