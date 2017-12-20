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

/**
 * @deprecated Use {@link com.example.trubin23.myfirstapplication.LoadNoteService} instead.
 */

@Deprecated
public class AsyncTaskGetNote extends AsyncTaskTableNote {

    private String mUid;

    public AsyncTaskGetNote(@NonNull LocalBroadcastManager broadcastManager,
                            @NonNull NoteDao noteDao, @NonNull String uid) {
        super(broadcastManager, noteDao);
        mUid = uid;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Note note = mNoteDao.getNote(mUid);
        List<Note> notes = new ArrayList<>();
        notes.add(note);

        Intent intent = new Intent(EditNoteActivity.ACTION_REFRESH_NOTE);
        intent.putParcelableArrayListExtra(EditNoteActivity.NOTE, new ArrayList<>(notes));
        mBroadcastManager.sendBroadcast(intent);

        return null;
    }
}
