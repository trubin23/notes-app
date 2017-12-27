package com.example.trubin23.myfirstapplication.presentation.notes.add;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trubin23.myfirstapplication.R;
import com.example.trubin23.myfirstapplication.MyCustomApplication;
import com.example.trubin23.myfirstapplication.presentation.common.BaseActivity;
import com.example.trubin23.myfirstapplication.presentation.notes.utils.ThemeChanger;
import com.example.trubin23.myfirstapplication.presentation.notes.utils.Utils;
import com.example.trubin23.myfirstapplication.storage.model.Note;
import com.example.trubin23.myfirstapplication.storage.database.NoteDao;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.trubin23.myfirstapplication.storage.database.DatabaseHelper.DEFAULT_ID;
import static com.example.trubin23.myfirstapplication.storage.model.Note.NOTE_UID;

public class EditNoteActivity extends BaseActivity implements EditNoteContract.View {

    public static final String TAG = "EditNoteActivity";
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
    private MenuItem mPickColorMenuItem;

    private String mNoteUid;
    private String mNoteColor;

    private NoteReceiver mNoteReceiver;
    private NoteState mNoteState;

    private static Disposable disposable;

    private EditNotePresenter mPresenter;

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

        createPresenter();

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
            mNoteState = NoteState.valueOf(savedInstanceState.getString(NOTE_STATE));
            changeNoteColor(savedInstanceState.getString(NOTE_COLOR));
        }

        mNoteReceiver = new NoteReceiver();
    }

    private void createPresenter() {
        mPresenter = new EditNotePresenter();
        bindPresenterToView(mPresenter);
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
        if (mEditTitle.getText().toString().trim().isEmpty() ||
                mEditText.getText().toString().trim().isEmpty()) {
            setMenuState(false, getResources().getColor(R.color.light_transparent));
        } else {
            setMenuState(true, getResources().getColor(R.color.white));
        }
    }

    private void setMenuState(boolean enabled, int tint) {
        if (mAcceptMenuItem != null) {
            mAcceptMenuItem.setEnabled(enabled);
            mAcceptMenuItem.getIcon().setTint(tint);
        }
        if (mPickColorMenuItem != null) {
            mPickColorMenuItem.setEnabled(enabled);
            mPickColorMenuItem.getIcon().setTint(tint);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note, menu);
        mAcceptMenuItem = menu.findItem(R.id.action_accept);
        mPickColorMenuItem = menu.findItem(R.id.action_pick_color);

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

            boolean addNote = Objects.equals(noteUid, DEFAULT_ID);
            mPresenter.saveNote(note, addNote);
        }

        onBackPressed();

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable = null;
    }

    private void pickNoteColor() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton(android.R.string.ok, (dialog, selectedColor, allColors) ->
                        changeNoteColor(String.format("#%06X", (0xFFFFFF & selectedColor))))
                .setNegativeButton(android.R.string.cancel, null)
                .build()
                .show();
    }

    private void changeNoteColor(String stringColor) {
        mNoteColor = stringColor;

        if (mNoteColor != null) {
            mEditTitle.setBackgroundColor(Color.parseColor(stringColor));
            mEditTitle.setTextColor(Utils.colorText(stringColor));

            mEditText.setBackgroundColor(Color.parseColor(stringColor));
            mEditText.setTextColor(Utils.colorText(stringColor));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mNoteReceiver,
                new IntentFilter(ACTION_GET_EDIT_NOTE));

        if (Objects.equals(mNoteUid, DEFAULT_ID)) {
            return;
        }

        mInfoTitle.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.GONE);

        if (mNoteState != NoteState.FINISH) {
            mProgressBar.setVisibility(View.VISIBLE);
            mEditText.setEnabled(false);
        }

        if (mNoteState == NoteState.START) {
            mNoteState = NoteState.PROCESS;

            if (disposable == null) {
                NoteDao noteDao = ((MyCustomApplication) getApplication()).getNoteDao();

                disposable = Single.create((SingleOnSubscribe<Note>) emitter ->
                        emitter.onSuccess(noteDao.getNote(mNoteUid)))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(note -> {
                            Intent intentResult = new Intent(ACTION_GET_EDIT_NOTE);
                            intentResult.putExtra(NOTE, note);
                            LocalBroadcastManager.getInstance(this).sendBroadcast(intentResult);
                        }, throwable -> {
                        });
            } else {
                String errorMessage = getResources().getString(R.string.failed_upload_note);
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                Log.e(TAG, "NoteState.START, disposable != null");
            }
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNoteReceiver);
        super.onPause();
    }

    @Override
    public void showSuccessToast(int eventId) {
        Resources res = getResources();
        Toast.makeText(this, res.getString(eventId) + "\n"
                + res.getString(R.string.success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorToast(int eventId, int errorCode) {
        Resources res = getResources();
        Toast.makeText(this, res.getString(eventId) + "\n"
                        + res.getString(R.string.error_code) + errorCode, Toast.LENGTH_SHORT).show();
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
