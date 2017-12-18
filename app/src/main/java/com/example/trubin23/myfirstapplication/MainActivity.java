package com.example.trubin23.myfirstapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.trubin23.database.asynctasktablenote.AsyncTaskDeleteNote;
import com.example.trubin23.database.asynctasktablenote.AsyncTaskRefreshNotes;
import com.example.trubin23.database.NoteDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NoteItemActionHandler {

    private static final String TAG = "MainActivity";

    static final String NOTE_ID = "note_id";

    public static final String ACTION_REFRESH_NOTES = "action-refresh-notes";
    public static final String NOTES = "notes";

    @BindView(R.id.rv) RecyclerView mRecyclerView;
    private RecyclerNoteAdapter mRecyclerNoteAdapter;

    private NotesReceiver mNotesReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChanger.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);

        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager llm = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(llm);
        } else {
            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(
                    2, StaggeredGridLayoutManager.VERTICAL);//number or const
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mRecyclerNoteAdapter = new RecyclerNoteAdapter(this);
        mRecyclerView.setAdapter(mRecyclerNoteAdapter);

        mNotesReceiver = new NotesReceiver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.theme_choicer, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.theme_choicer == item.getItemId()) {
            new ThemeChanger(this).showDialog();
        }

        return true;
    }

    @OnClick(R.id.button_create_note)
    public void onClickCreateNote(View view) {
        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        startActivity(intent);
    }

    @Override
    public void onEdit(@NonNull Note note) {
        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        intent.putExtra(NOTE_ID, note.getId());
        startActivity(intent);
    }

    @Override
    public void onDelete(@NonNull final Note note, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(note.getTitle());
        builder.setMessage(R.string.message_about_delete);

        builder.setPositiveButton(getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LocalBroadcastManager broadcastManager =
                                LocalBroadcastManager.getInstance(MainActivity.this);
                        NoteDao noteDao = ((MyCustomApplication)getApplication()).getNoteDao();

                        AsyncTaskDeleteNote deleteNote =
                                new AsyncTaskDeleteNote(broadcastManager, noteDao, note.getId());
                        deleteNote.execute();
                    }
                });

        builder.setNegativeButton(getString(android.R.string.cancel), null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mNotesReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mNotesReceiver, new IntentFilter(ACTION_REFRESH_NOTES));

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        NoteDao noteDao = ((MyCustomApplication)getApplication()).getNoteDao();

        AsyncTaskRefreshNotes refreshNotes =
                new AsyncTaskRefreshNotes(broadcastManager, noteDao);
        refreshNotes.execute();

        super.onResume();
    }

    private class NotesReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<Note> notes = intent.getParcelableArrayListExtra(NOTES);
            mRecyclerNoteAdapter.setNotes(notes);
        }
    };
}