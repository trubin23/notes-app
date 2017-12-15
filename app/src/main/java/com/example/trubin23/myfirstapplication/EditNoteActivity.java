package com.example.trubin23.myfirstapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.example.trubin23.database.AsyncTaskAddNote;
import com.example.trubin23.database.AsyncTaskUpdateNote;
import com.example.trubin23.database.NoteDao;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.trubin23.database.DatabaseHelper.DEFAULT_ID;

public class EditNoteActivity extends AppCompatActivity implements AsyncResponse {

    @BindView(R.id.info_title)
    TextView mInfoTitle;
    @BindView(R.id.edit_title)
    EditText mEditTitle;
    @BindView(R.id.edit_text)
    EditText mEditText;
    @BindView(R.id.indeterminate_bar)
    ProgressBar mProgressBar;

    private MenuItem mAcceptMenuItem;

    private long mNoteId;

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
        mNoteId = intent.getLongExtra(MainActivity.NOTE_ID, DEFAULT_ID);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            if (mNoteId != DEFAULT_ID) {
                actionBar.setTitle(R.string.load_note);
            } else {
                actionBar.setTitle(R.string.new_note);
            }
        }

        if (mNoteId != DEFAULT_ID) {
            mInfoTitle.setVisibility(View.GONE);
            mEditTitle.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            mEditText.setEnabled(false);

            NoteDao noteDao = ((MyCustomApplication) getApplication()).getNoteDao();

            AsyncTaskGetNote asyncTaskGetNote =
                    new AsyncTaskGetNote(noteDao, mNoteId, this);
            asyncTaskGetNote.execute();
        }
    }

    private void validNote() {
        if (mAcceptMenuItem != null) {
            if (mEditTitle.getText().toString().trim().isEmpty() ||
                    mEditText.getText().toString().trim().isEmpty()) {
                mAcceptMenuItem.setEnabled(false);
                mAcceptMenuItem.getIcon().setTint(getResources().getColor(
                        R.color.light_transparent));
            } else {
                mAcceptMenuItem.setEnabled(true);
                mAcceptMenuItem.getIcon().setTint(getResources().getColor(
                        android.R.color.white));
            }
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

            Note note = new Note(mNoteId, mEditTitle.getText().toString(),
                    mEditText.getText().toString(), null);

            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
            NoteDao noteDao = ((MyCustomApplication)getApplication()).getNoteDao();

            if (mNoteId == DEFAULT_ID){
                AsyncTaskAddNote addNote =
                        new AsyncTaskAddNote(broadcastManager, noteDao, note);
                addNote.execute();
            } else {
                AsyncTaskUpdateNote updateNote =
                        new AsyncTaskUpdateNote(broadcastManager, noteDao, note);
                updateNote.execute();
            }

            Intent intent = new Intent();
            intent.putExtra(MainActivity.NOTE, note);

            setResult(RESULT_OK, intent);
        }

        finish();

        return true;
    }

    @Override
    public void updateViews(@Nullable Note note) {
        if (note == null) {
            return;
        }

        mEditTitle.setText(note.getTitle());
        mEditText.setText(note.getText());
        mEditText.setEnabled(true);
        mProgressBar.setVisibility(View.GONE);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(note.getTitle());
        }
    }

    private static class AsyncTaskGetNote extends AsyncTask<Void, Void, Note> {

        private NoteDao mNoteDao;
        private long mNoteId;
        private AsyncResponse mAsyncResponse;

        AsyncTaskGetNote(@NonNull NoteDao noteDao, long noteId,
                         @NonNull AsyncResponse asyncResponse) {
            mNoteDao = noteDao;
            mNoteId = noteId;
            mAsyncResponse = asyncResponse;
        }

        @Nullable
        @Override
        protected Note doInBackground(Void... voids) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mNoteDao.getNote(mNoteId);
        }

        @Override
        protected void onPostExecute(@Nullable Note note) {
            super.onPostExecute(note);

            mAsyncResponse.updateViews(note);
        }
    }

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




