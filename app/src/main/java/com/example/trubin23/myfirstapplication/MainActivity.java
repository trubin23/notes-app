package com.example.trubin23.myfirstapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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

    private DBNotes mDBNotes;

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
                                if (mDBNotes.deleteNote(note)) {
                                    mRecyclerNoteAdapter.deleteNote(itemPosition);
                                }
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

        mDBNotes = new DBNotes(this);

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
        if (notes == null) { // (savedInstanceState!=null) not mean (notes!=null)
            notes = mDBNotes.getAllNote();
        }

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mRecyclerNoteAdapter = new RecyclerNoteAdapter(notes, mOnClickEditNote, mOnLongClickDeleteNote);
        mRecyclerView.setAdapter(mRecyclerNoteAdapter);

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
            int itemPosition = intent.getIntExtra(
                    ITEM_POSITION, ITEM_POSITION_DEFAULT);

            Note note = intent.getParcelableExtra(NOTE);
            if (itemPosition != ITEM_POSITION_DEFAULT) {
                if (mDBNotes.updateNote(note)){
                    mRecyclerNoteAdapter.updateNote(itemPosition, note);
                }
            } else {
                Note noteAdd = mDBNotes.addNote(note);
                if (noteAdd!=null) {
                    mRecyclerNoteAdapter.addNote(noteAdd);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(NOTES,
                new ArrayList<Parcelable>(mRecyclerNoteAdapter.getNotes()));
    }
}