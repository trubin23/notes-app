package com.example.trubin23.myfirstapplication.presentation.notes.add;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trubin23.myfirstapplication.R;
import com.example.trubin23.myfirstapplication.domain.notes.usecase.AddNoteUseCase;
import com.example.trubin23.myfirstapplication.domain.notes.usecase.GetNoteUseCase;
import com.example.trubin23.myfirstapplication.domain.notes.usecase.UpdateNoteUseCase;
import com.example.trubin23.myfirstapplication.presentation.common.BaseActivity;
import com.example.trubin23.myfirstapplication.presentation.notes.model.NoteView;
import com.example.trubin23.myfirstapplication.presentation.notes.utils.ThemeChanger;
import com.example.trubin23.myfirstapplication.presentation.notes.utils.Utils;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.trubin23.myfirstapplication.storage.model.NoteStorage.NOTE_UID;

public class EditNoteActivity extends BaseActivity implements EditNoteContract.View {

    private static final String ACTION_BAR_TITLE = "action_bar_title";
    private static final String NOTE_COLOR = "note_color";

    @BindView(R.id.info_title)
    TextView mInfoTitle;
    @BindView(R.id.edit_title)
    EditText mEditTitle;
    @BindView(R.id.edit_text)
    EditText mEditText;

    private MenuItem mAcceptMenuItem;
    private MenuItem mPickColorMenuItem;

    private String mNoteUid;
    private String mNoteColor;

    private EditNotePresenter mPresenter;

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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            if (savedInstanceState == null) {
                actionBar.setTitle(R.string.new_note);
            } else {
                actionBar.setTitle(savedInstanceState.getCharSequence(ACTION_BAR_TITLE));
            }
        }

        if (mNoteUid != null){
            mInfoTitle.setVisibility(View.GONE);
            mEditTitle.setVisibility(View.GONE);
        }

        if (savedInstanceState == null) {
            if (mNoteUid != null) {
                mPresenter.loadNote(mNoteUid);
            }
        } else {
            changeNoteColor(savedInstanceState.getString(NOTE_COLOR));
        }
    }

    private void createPresenter() {
        AddNoteUseCase addNoteUseCase = new AddNoteUseCase();
        UpdateNoteUseCase updateNoteUseCase = new UpdateNoteUseCase();
        GetNoteUseCase getNoteUseCase = new GetNoteUseCase();
        mPresenter = new EditNotePresenter(mUseCaseHandler, addNoteUseCase,
                updateNoteUseCase, getNoteUseCase);
        bindPresenterToView(mPresenter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            outState.putCharSequence(ACTION_BAR_TITLE, actionBar.getTitle());
        }

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
        }

        if (R.id.action_accept == item.getItemId()) {
            NoteView noteView = new NoteView(mEditTitle.getText().toString(),
                                                   mEditText.getText().toString(), mNoteColor);

            boolean addNote = mNoteUid == null;
            mPresenter.saveNote(noteView, addNote);
        }

        if (item.getItemId() == android.R.id.home){
            finish();
        }

        return true;
    }

    @Override
    public void savingDbComplete() {
        onBackPressed();
    }

    @Override
    public void setNote(@NonNull NoteView noteView) {
        mEditTitle.setText(noteView.getTitle());
        mEditText.setText(noteView.getContent());

        changeNoteColor(noteView.getColor());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(noteView.getTitle());
        }
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
    public void showSuccessToast(int eventId) {
        Resources res = getResources();
        Toast.makeText(this, res.getString(eventId) + "\n"
                + res.getString(R.string.success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorToast(int eventId) {
        Resources res = getResources();
        Toast.makeText(this, res.getString(eventId) + "\n"
                        + res.getString(R.string.error), Toast.LENGTH_SHORT).show();
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
