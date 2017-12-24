package com.example.trubin23.database.asynctasktablenote;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.example.trubin23.database.NoteDao;
import com.example.trubin23.myfirstapplication.EditNoteActivity;
import com.example.trubin23.database.Note;

/**
 * Created by Andrey on 18.12.2017.
 */

/**
 * @deprecated Use {@link com.example.trubin23.myfirstapplication.LoadNoteService} instead.
 */

@Deprecated
public class AsyncTaskGetNote extends AsyncTaskBase {

    private String mUid;

    public AsyncTaskGetNote(@NonNull LocalBroadcastManager broadcastManager,
                            @NonNull NoteDao noteDao, @NonNull String uid) {
        super(broadcastManager, noteDao);
        mUid = uid;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Note note = mNoteDao.getNote(mUid);

        Intent intent = new Intent(EditNoteActivity.ACTION_GET_EDIT_NOTE);
        intent.putExtra(EditNoteActivity.NOTE, note);
        mBroadcastManager.sendBroadcast(intent);

        return null;
    }
}
