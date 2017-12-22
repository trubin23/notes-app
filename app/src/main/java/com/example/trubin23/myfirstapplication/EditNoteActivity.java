package com.example.trubin23.myfirstapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
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
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.trubin23.database.DatabaseHelper.DEFAULT_ID;
import static com.example.trubin23.myfirstapplication.Note.NOTE_UID;

public class EditNoteActivity extends AppCompatActivity {

    public static final String ACTION_GET_EDIT_NOTE = "action-get-edit-note";
    public static final String NOTE = "note";

    private static final String ACTION_BAR_TITLE = "action_bar_title";
    private static final String NOTE_STATE = "note_state";
    private static final String NOTE_COLOR = "note_color";

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
    private String mNoteColor;

    private NoteReceiver mNoteReceiver;
    private NoteState mNoteState;

    private enum NoteState {
        START,
        PROCESS,
        FINISH
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
        if (mNoteUid == null) {
            mNoteUid = DEFAULT_ID;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            if (savedInstanceState == null) {
                if (Objects.equals(mNoteUid, DEFAULT_ID)) {
                    actionBar.setTitle(R.string.new_note);
                } else {
                    actionBar.setTitle(R.string.load_note);
                }
            } else {
                actionBar.setTitle(savedInstanceState.getCharSequence(ACTION_BAR_TITLE));
            }
        }

        if (savedInstanceState == null) {
            mNoteState = NoteState.START;
        } else {
            mNoteState = NoteState.valueOf(
                    savedInstanceState.getString(NOTE_STATE));
            mNoteColor = savedInstanceState.getString(NOTE_COLOR);
        }

        mNoteReceiver = new NoteReceiver();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            outState.putCharSequence(ACTION_BAR_TITLE, actionBar.getTitle());
        }

        outState.putString(NOTE_STATE, mNoteState.toString());
        outState.putString(NOTE_COLOR, mNoteColor);

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
        if (R.id.action_pick_color == item.getItemId()) {
            pickNoteColor();
            return true;
        }

        if (R.id.action_accept == item.getItemId()) {
            String noteUid = mNoteUid;
            if (Objects.equals(mNoteUid, DEFAULT_ID)) {
                noteUid = UUID.randomUUID().toString();
            }
            Note note = new Note(noteUid, mEditTitle.getText().toString(),
                    mEditText.getText().toString(), mNoteColor, null);

            NoteDao noteDao = ((MyCustomApplication) getApplication()).getNoteDao();

            if (Objects.equals(mNoteUid, DEFAULT_ID)) {
                SyncWithServer.addNote(getApplicationContext(), noteDao, note);
            } else {
                SyncWithServer.updateNote(getApplicationContext(), noteDao, note);
            }
        }

        finish();

        return true;
    }

    private void pickNoteColor() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton(android.R.string.ok, new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int selectedColor, Integer[] allColors) {
                        changeNoteColor(String.format("#%06X", (0xFFFFFF & selectedColor)));
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .build()
                .show();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mNoteReceiver, new IntentFilter(ACTION_GET_EDIT_NOTE));

        if (!Objects.equals(mNoteUid, DEFAULT_ID)) {
            mInfoTitle.setVisibility(View.GONE);
            mEditTitle.setVisibility(View.GONE);

            if (mNoteState != NoteState.FINISH) {
                mProgressBar.setVisibility(View.VISIBLE);
                mEditText.setEnabled(false);
            }

            if (mNoteState == NoteState.START) {
                Intent intent = new Intent(this, LoadNoteService.class);
                intent.putExtra(NOTE_UID, mNoteUid);

                startService(intent);

                mNoteState = NoteState.PROCESS;
            }
        }

        super.onResume();
    }

    private void changeNoteColor(String stringColor) {
        mNoteColor = stringColor;

        mEditTitle.setBackgroundColor(Color.parseColor(stringColor));
        mEditTitle.setTextColor(Utils.colorText(stringColor));

        mEditText.setBackgroundColor(Color.parseColor(stringColor));
        mEditText.setTextColor(Utils.colorText(stringColor));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNoteReceiver);
        super.onPause();
    }

    private class NoteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Note note = intent.getParcelableExtra(NOTE);
            if (note == null) {
                return;
            }

            mNoteState = NoteState.FINISH;

            mEditTitle.setText(note.getTitle());
            mEditText.setText(note.getContent());
            mEditText.setEnabled(true);
            mProgressBar.setVisibility(View.GONE);

            changeNoteColor(note.getColor());

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(note.getTitle());
            }
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
