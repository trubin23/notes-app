package com.example.trubin23.myfirstapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    static final String NOTE = "note";
    static final String NOTES = "notes";
    static final String ITEM_POSITION = "item_position";

    public static final int ITEM_POSITION_DEFAULT = -1;

    @BindView(R.id.rv) RecyclerView mRecyclerView;
    private RecyclerNoteAdapter mRecyclerNoteAdapter;

    private View.OnClickListener mOnClickCreateNote = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
            startActivityForResult(intent, 0);
        }
    };

    private View.OnClickListener mOnClickEditNote = new View.OnClickListener() {
        @Override
        public void onClick(View itemView) {
            int itemPosition = mRecyclerView.getLayoutManager().getPosition(itemView);

            Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
            intent.putExtra(ITEM_POSITION, itemPosition);
            intent.putExtra(NOTE, mRecyclerNoteAdapter.getItem(itemPosition));
            startActivityForResult(intent, 0);
        }
    };

    private View.OnLongClickListener mOnLongClickDeleteNote = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View itemView) {
            final int itemPosition = mRecyclerView.getLayoutManager().getPosition(itemView);

            final Note note = mRecyclerNoteAdapter.getItem(itemPosition);

            if (note != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle(note.getTitle());
                builder.setMessage(R.string.message_about_delete);

                builder.setPositiveButton(getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseConnector databaseConnector = ((MyCustomApplication)getApplication()).getDBNotes();
                                databaseConnector.deleteNote(note.getId());
                                mRecyclerNoteAdapter.deleteNote(itemPosition);
                            }
                        });

                builder.setNegativeButton(getString(android.R.string.cancel), null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChangeTheme.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);

        if (this.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager llm = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(llm);
        } else {
            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(
                    2, StaggeredGridLayoutManager.VERTICAL);//number or const
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }

        List<Note> notes = null;
        if (savedInstanceState != null) {
            notes = savedInstanceState.getParcelableArrayList(NOTES);
        }

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mRecyclerNoteAdapter = new RecyclerNoteAdapter(notes, mOnClickEditNote, mOnLongClickDeleteNote);
        mRecyclerView.setAdapter(mRecyclerNoteAdapter);

        if (notes == null) { // (savedInstanceState!=null) not mean (notes!=null)
            updateRecyclerNote();
        }

        FloatingActionButton buttonCreateNote = findViewById(R.id.button_create_note);
        buttonCreateNote.setOnClickListener(mOnClickCreateNote);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.choice_theme, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.item_theme == item.getItemId()) {
            new ChangeTheme(this);
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            updateRecyclerNote();
        }
    }

    private void updateRecyclerNote(){
        DatabaseConnector databaseConnector = ((MyCustomApplication)getApplication()).getDBNotes();

        AsyncTaskRecyclerNote asyncTask =
                new AsyncTaskRecyclerNote(mRecyclerNoteAdapter, databaseConnector);
        asyncTask.execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(NOTES,
                new ArrayList<Parcelable>(mRecyclerNoteAdapter.getNotes()));
    }

    private static class AsyncTaskRecyclerNote extends AsyncTask<Void, Void, List<Note>>{

        private RecyclerNoteAdapter mRecyclerNoteAdapter;
        private DatabaseConnector mDatabaseConnector;

        AsyncTaskRecyclerNote(RecyclerNoteAdapter recyclerNoteAdapter,
                                     DatabaseConnector databaseConnector) {
            this.mRecyclerNoteAdapter = recyclerNoteAdapter;
            this.mDatabaseConnector = databaseConnector;
        }

        @Override
        protected List<Note> doInBackground(Void... voids) {
            return mDatabaseConnector.getAllNote();
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            super.onPostExecute(notes);

            mRecyclerNoteAdapter.setNotes(notes);
        }
    }
}