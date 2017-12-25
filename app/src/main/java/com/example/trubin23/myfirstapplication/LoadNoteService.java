package com.example.trubin23.myfirstapplication;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.trubin23.database.Note;
import com.example.trubin23.database.NoteDao;

import static com.example.trubin23.database.Note.NOTE_UID;

/**
 * Created by trubin23 on 18.12.17.
 */

public class LoadNoteService extends IntentService {

    private static final String NAME_WORKER_THREAD = "load_note_service";

    private boolean mWorked;

    public LoadNoteService() {
        super(NAME_WORKER_THREAD);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWorked = true;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }

        String noteUid = intent.getStringExtra(NOTE_UID);
        if (noteUid == null){
            return;
        }

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        NoteDao noteDao = ((MyCustomApplication)getApplication()).getNoteDao();

        Note note = noteDao.getNote(noteUid);

        Intent intentResult = new Intent(EditNoteActivity.ACTION_GET_EDIT_NOTE);
        intentResult.putExtra(EditNoteActivity.NOTE, note);
        if (mWorked) {
            broadcastManager.sendBroadcast(intentResult);
        }
    }

    @Override
    public void onDestroy() {
        mWorked = false;
        super.onDestroy();
    }
}
