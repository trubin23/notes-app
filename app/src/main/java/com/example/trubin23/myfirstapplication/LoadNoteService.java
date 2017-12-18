package com.example.trubin23.myfirstapplication;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.trubin23.database.NoteDao;

import java.util.ArrayList;
import java.util.List;

import static com.example.trubin23.database.DatabaseHelper.DEFAULT_ID;
import static com.example.trubin23.myfirstapplication.Note.NOTE_ID;

/**
 * Created by trubin23 on 18.12.17.
 */

public class LoadNoteService extends IntentService {

    private static final String NAME_WORKER_THREAD = "load_note_service";

    public LoadNoteService() {
        super(NAME_WORKER_THREAD);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }

        long noteId = intent.getLongExtra(NOTE_ID, DEFAULT_ID);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        NoteDao noteDao = ((MyCustomApplication)getApplication()).getNoteDao();

        Note note = noteDao.getNote(noteId);

        List<Note> notes = new ArrayList<>();
        notes.add(note);

        Intent intentResult = new Intent(EditNoteActivity.ACTION_REFRESH_NOTE);
        intentResult.putParcelableArrayListExtra(EditNoteActivity.NOTE, new ArrayList<>(notes));
        broadcastManager.sendBroadcast(intentResult);
    }
}
