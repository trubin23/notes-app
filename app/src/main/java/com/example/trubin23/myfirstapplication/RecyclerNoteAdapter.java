package com.example.trubin23.myfirstapplication;

import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trubin23.database.NoteDao;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by skyfishjy on 10/31/14.
 */
public class RecyclerNoteAdapter extends CursorRecyclerViewAdapter<RecyclerNoteAdapter.NoteHolder> {

    private NoteItemActionHandler mActionHandler;

    RecyclerNoteAdapter(@Nullable NoteItemActionHandler actionHandler) {
        super();
        mActionHandler = actionHandler;
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NoteHolder holder, Cursor cursor) {
        cursor.moveToPosition(cursor.getPosition());
        holder.setData(cursor);

        final String uid = cursor.getString(cursor.getColumnIndex(NoteDao.COLUMN_NOTE_UID));

        holder.itemView.setOnClickListener(view -> {
            if (mActionHandler != null) {
                mActionHandler.onEdit(uid);
            }
        });

        holder.itemView.setOnLongClickListener(view -> {
            if (mActionHandler != null) {
                final int itemPosition = holder.getAdapterPosition();
                String title = holder.noteTitle.getText().toString();
                mActionHandler.onDelete(uid, title, itemPosition);

                return true;
            } else {
                return false;
            }
        });
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.note_title)
        TextView noteTitle;

        @BindView(R.id.note_text)
        TextView noteText;

        NoteHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(Cursor cursor) {
            noteTitle.setText(cursor.getString(cursor.getColumnIndex(NoteDao.COLUMN_NOTE_TITLE)));
            noteText.setText(cursor.getString(cursor.getColumnIndex(NoteDao.COLUMN_NOTE_CONTENT)));

            String color = cursor.getString(cursor.getColumnIndex(NoteDao.COLUMN_NOTE_COLOR));
            noteTitle.setTextColor(Utils.colorText(color));
            noteText.setTextColor(Utils.colorText(color));
            itemView.setBackgroundColor(Color.parseColor(color));

            //Date destroyDate = new Date((long) note.getDestroyDate() * 1000);
            //noteDate.setText(destroyDate.toString());
        }
    }
}