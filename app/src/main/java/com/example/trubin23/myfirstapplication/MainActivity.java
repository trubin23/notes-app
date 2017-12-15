package com.example.trubin23.myfirstapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.trubin23.database.NoteDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NoteItemActionHandler {

    private static final String TAG = "MainActivity";

    static final String NOTE_ID = "note_id";
    static final String NOTE = "note";

    public static final int REQUEST_CODE_CREATE_NOTE = 0;
    public static final int REQUEST_CODE_EDIT_NOTE = 1;

    @BindView(R.id.rv) RecyclerView mRecyclerView;
    private RecyclerNoteAdapter mRecyclerNoteAdapter;

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

        recyclerNoteChange(null);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != RESULT_OK) {
            return;
        }

        final NoteDao noteDao = ((MyCustomApplication)getApplication()).getNoteDao();
        final Note note = intent.getParcelableExtra(NOTE);

        Runnable runnable = null;
        switch (requestCode){
            case REQUEST_CODE_CREATE_NOTE:
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        noteDao.addNote(note);
                    }
                };
                break;
            case REQUEST_CODE_EDIT_NOTE:
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        noteDao.updateNote(note);
                    }
                };
                break;
            default:
                Log.w(TAG, "unknown value requestCode in method onActivityResult");
        }
        recyclerNoteChange(runnable);
    }

    @OnClick(R.id.button_create_note)
    public void onClickCreateNote(View view) {
        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CREATE_NOTE);
    }

    void recyclerNoteChange(@Nullable Runnable runnable){
        if (runnable == null){
            runnable = new Runnable() {
                @Override
                public void run() {
                }
            };
        }

        NoteDao noteDao = ((MyCustomApplication)getApplication()).getNoteDao();

        AsyncTaskRecyclerNote asyncTask =
                new AsyncTaskRecyclerNote(mRecyclerNoteAdapter, noteDao, runnable);
        asyncTask.execute();
    }

    @Override
    public void onEdit(@NonNull Note note) {
        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        intent.putExtra(NOTE_ID, note.getId());
        startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE);
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
                        Runnable runnable = new Runnable() {
                            private final NoteDao noteDao =
                                    ((MyCustomApplication)getApplication()).getNoteDao();

                            @Override
                            public void run() {
                                noteDao.deleteNote(note.getId());
                            }
                        };
                        recyclerNoteChange(runnable);
                    }
                });

        builder.setNegativeButton(getString(android.R.string.cancel), null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private static class AsyncTaskRecyclerNote extends AsyncTask<Void, Void, List<Note>>{

        private RecyclerNoteAdapter mRecyclerNoteAdapter;
        private NoteDao mNoteDao;
        private Runnable mRunnable;

        AsyncTaskRecyclerNote(@NonNull RecyclerNoteAdapter recyclerNoteAdapter,
                                     @NonNull NoteDao noteDao, @NonNull Runnable runnable) {
            mRecyclerNoteAdapter = recyclerNoteAdapter;
            mNoteDao = noteDao;
            mRunnable = runnable;
        }

        @NonNull
        @Override
        protected List<Note> doInBackground(Void... voids) {
            mRunnable.run();
            return mNoteDao.getAllNote();
        }

        @Override
        protected void onPostExecute(@NonNull List<Note> notes) {
            super.onPostExecute(notes);

            mRecyclerNoteAdapter.setNotes(notes);
        }
    }
}