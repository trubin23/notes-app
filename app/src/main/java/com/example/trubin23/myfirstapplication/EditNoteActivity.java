package com.example.trubin23.myfirstapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.trubin23.database.NoteDaoImpl;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditNoteActivity extends AppCompatActivity {

    @BindView(R.id.edit_note_title)
    EditText mEditTitle;
    @BindView(R.id.edit_note_text)
    EditText mEditText;

    private int mItemPosition = MainActivity.ITEM_POSITION_DEFAULT;

    private Note mNote;

    private MenuItem mMenuItemAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChangeTheme.onActivityCreateSetTheme(this);
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
        mItemPosition = intent.getIntExtra(MainActivity.ITEM_POSITION,
                MainActivity.ITEM_POSITION_DEFAULT);

        mNote = intent.getParcelableExtra(MainActivity.NOTE);
        if (mNote != null) {
            TextView textViewTitle = findViewById(R.id.text_view_title);

            textViewTitle.setVisibility(View.GONE);
            mEditTitle.setVisibility(View.GONE);

            mEditText.setText(mNote.getText());
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            if (mNote != null) {
                mEditTitle.setText(mNote.getTitle());
                actionBar.setTitle(mNote.getTitle());
            } else {
                actionBar.setTitle(R.string.new_note);
            }
        }
    }

    private void validNote() {
        if (mMenuItemAccept != null) {
            if (mEditTitle.getText().toString().trim().isEmpty() ||
                    mEditText.getText().toString().trim().isEmpty()) {
                mMenuItemAccept.setVisible(false);//.setEnabled(false);
            } else {
                mMenuItemAccept.setVisible(true);//.setEnabled(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note, menu);
        mMenuItemAccept = menu.findItem(R.id.item_accept);

        validNote();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.item_accept == item.getItemId()) {

            NoteDaoImpl noteDaoImpl = ((MyCustomApplication)getApplication()).getDBNotes();

            if (mNote != null) {//change if ?
                mNote.setText(mEditText.getText().toString());
                mNote.setDate(new Date());

	            noteDaoImpl.updateNote(mNote);
            } else {
                mNote = new Note(mEditTitle.getText().toString(),
                        mEditText.getText().toString());
	            noteDaoImpl.addNote(mNote);
            }

            setResult(RESULT_OK, null);
        }

        finish();

        return true;
    }

    static class SimpleTextWatcher implements TextWatcher {
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
