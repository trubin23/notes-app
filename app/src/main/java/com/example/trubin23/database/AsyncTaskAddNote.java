package com.example.trubin23.database;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.trubin23.myfirstapplication.MainActivity;
import com.example.trubin23.myfirstapplication.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trubin23 on 15.12.17.
 */

public class AsyncTaskAddNote extends AsyncTaskTableNote {

    private Note mNote;

    public AsyncTaskAddNote(LocalBroadcastManager broadcastManager,
                            NoteDao noteDao, Note note) {
        super(broadcastManager, noteDao);
        mNote = note;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        mNoteDao.addNote(mNote);
        List<Note> notes = mNoteDao.getAllNote();

        Intent intent = new Intent(MainActivity.ACTION_REFRESH_NOTES);
        intent.putParcelableArrayListExtra(MainActivity.NOTES, new ArrayList<Note>(notes));
        mBroadcastManager.sendBroadcast(intent);

        return null;
    }
}
