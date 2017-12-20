package com.example.trubin23.myfirstapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.trubin23.database.NoteDao;
import com.example.trubin23.database.asynctasktablenote.AsyncTaskAddNote;
import com.example.trubin23.database.asynctasktablenote.AsyncTaskUpdateNote;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.trubin23.database.DatabaseHelper.DEFAULT_ID;
import static com.example.trubin23.myfirstapplication.Note.NOTE_UID;

public class EditNoteActivity extends AppCompatActivity {

    public static final String ACTION_REFRESH_NOTE = "action-refresh-note";
    public static final String NOTE = "note";

    private static final String ACTION_BAR_TITLE = "action_bar_title";
    private static final String LOAD_NOTE_STATE = "load_note_state";

    @BindView(R.id.info_title)
    TextView mInfoTitle;
    @BindView(R.id.edit_title)
    EditText mEditTitle;
    @BindView(R.id.edit_text)
    EditText mEditText;
    @BindView(R.id.indeterminate_bar)
    ProgressBar mProgressBar;

    private MenuItem mAcceptMenuItem;
    private String mNoteUid;

    private NoteReceiver mNoteReceiver;

    private LoadNote mLoadNote;

    private enum LoadNote {
        START,
        PROCESS,
        FINISH;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChanger.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_edit_note);
        ButterKnife.bind(this);

        mEditTitle.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validNote();
            }
        });
        mEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validNote();
            }
        });

        Intent intent = getIntent();
        mNoteUid = intent.getStringExtra(NOTE_UID);
        if (mNoteUid == null){
            mNoteUid = DEFAULT_ID;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            if (savedInstanceState==null) {
                if (mNoteUid == DEFAULT_ID) {
                    actionBar.setTitle(R.string.new_note);
                } else {
                    actionBar.setTitle(R.string.load_note);
                }
            } else {
                actionBar.setTitle(savedInstanceState.getCharSequence(ACTION_BAR_TITLE));
            }
        }

        if (savedInstanceState==null) {
            mLoadNote = LoadNote.START;
        } else {
            mLoadNote = LoadNote.valueOf(
                    savedInstanceState.getString(LOAD_NOTE_STATE));
        }

        mNoteReceiver = new NoteReceiver();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            outState.putCharSequence(ACTION_BAR_TITLE, actionBar.getTitle());
        }

        outState.putString(LOAD_NOTE_STATE, mLoadNote.toString());

        super.onSaveInstanceState(outState);
    }

    private void validNote() {
        if (mAcceptMenuItem == null) {
            return;
        }

        if (mEditTitle.getText().toString().trim().isEmpty() ||
                mEditText.getText().toString().trim().isEmpty()) {
            mAcceptMenuItem.setEnabled(false);
            mAcceptMenuItem.getIcon().setTint(getResources().getColor(
                    R.color.light_transparent));
        } else {
            mAcceptMenuItem.setEnabled(true);
            mAcceptMenuItem.getIcon().setTint(getResources().getColor(
                    R.color.white));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note, menu);
        mAcceptMenuItem = menu.findItem(R.id.action_accept);

        validNote();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_accept == item.getItemId()) {
            Note note = new Note(mNoteUid, mEditTitle.getText().toString(),
                    mEditText.getText().toString(), null, null);

            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
            NoteDao noteDao = ((MyCustomApplication)getApplication()).getNoteDao();

            if (Objects.equals(mNoteUid, DEFAULT_ID)){
                AsyncTaskAddNote addNote =
                        new AsyncTaskAddNote(broadcastManager, noteDao, note);
                addNote.execute();
            } else {
                AsyncTaskUpdateNote updateNote =
                        new AsyncTaskUpdateNote(broadcastManager, noteDao, note);
                updateNote.execute();
            }
        }

        finish();

        return true;
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNoteReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mNoteReceiver, new IntentFilter(ACTION_REFRESH_NOTE));

        if (mNoteUid != DEFAULT_ID) {
            mInfoTitle.setVisibility(View.GONE);
            mEditTitle.setVisibility(View.GONE);

            if (mLoadNote != LoadNote.FINISH) {
                mProgressBar.setVisibility(View.VISIBLE);
                mEditText.setEnabled(false);
            }

            if(mLoadNote == LoadNote.START) {
                Intent intent = new Intent(this, LoadNoteService.class);
                intent.putExtra(NOTE_UID, mNoteUid);

                startService(intent);

                mLoadNote = LoadNote.PROCESS;
            }
        }

        super.onResume();
    }

    private class NoteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<Note> notes = intent.getParcelableArrayListExtra(NOTE);

            if (notes == null) {
                return;
            }

            if (notes.isEmpty()) {
                return;
            }

            Note note = notes.get(0);
            if (note == null) {
                return;
            }

            mLoadNote = LoadNote.FINISH;

            mEditTitle.setText(note.getTitle());
            mEditText.setText(note.getContent());
            mEditText.setEnabled(true);
            mProgressBar.setVisibility(View.GONE);

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(note.getTitle());
            }
        }
    };

    private static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
