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
 * Created by trubin23 on 20.12.17.
 */

public class AsyncTaskAddNotes extends AsyncTaskTableNote {

    private List<Note> mNotes;

    public AsyncTaskAddNotes(@NonNull LocalBroadcastManager broadcastManager,
                            @NonNull NoteDao noteDao, @NonNull List<Note> notes) {
        super(broadcastManager, noteDao);
        mNotes = notes;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (Note note : mNotes) {
            mNoteDao.addNote(note);
        }
        List<Note> notes = mNoteDao.getAllNote();

        Intent intent = new Intent(MainActivity.ACTION_REFRESH_NOTES);
        intent.putParcelableArrayListExtra(MainActivity.NOTES, new ArrayList<>(notes));
        mBroadcastManager.sendBroadcast(intent);

        return null;
    }
}
