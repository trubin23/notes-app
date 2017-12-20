package com.example.trubin23.myfirstapplication;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trubin23.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by trubin23 on 29.11.17.
 */

public class RecyclerNoteAdapter extends RecyclerView.Adapter<RecyclerNoteAdapter.NoteHolder> {

    private List<Note> mNotes;
    private NoteItemActionHandler mActionHandler;

    RecyclerNoteAdapter(@Nullable NoteItemActionHandler actionHandler) {
        mNotes = new ArrayList<>();
        mActionHandler = actionHandler;
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(final NoteHolder holder, int position) {
        final Note note = mNotes.get(position);

        holder.noteTitle.setText(note.getTitle());
        holder.noteText.setText(note.getContent());
        Date destroyDate = new Date((long) note.getDestroyDate() * 1000);
        holder.noteDate.setText(destroyDate.toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActionHandler != null) {
                    mActionHandler.onEdit(note);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mActionHandler != null) {
                    final int itemPosition = holder.getAdapterPosition();
                    mActionHandler.onDelete(note, itemPosition);

                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    @Nullable
    Note getItem(int position) {
        return mNotes.get(position);
    }

    @NonNull
    List<Note> getNotes() {
        return mNotes;
    }

    /**
     * Sets a new list of notes in the RecyclerView
     *
     * @param notes note.getUid() must not be equal to DatabaseHelper.DEFAULT_ID
     */
    void setNotes(@Nullable List<Note> notes) {
        mNotes = new ArrayList<>();

        if (notes != null) {
            for (Note note : notes) {
                if (!Objects.equals(note.getUid(), DatabaseHelper.DEFAULT_ID)) {
                    mNotes.add(note);
                }
            }
        }

        notifyDataSetChanged();
    }

    boolean addNote(@NonNull Note note) {
        if (!Objects.equals(note.getUid(), DatabaseHelper.DEFAULT_ID)) {
            mNotes.add(note);
            notifyItemInserted(mNotes.size() - 1);

            return true;
        } else {
            return false;
        }
    }

    void updateNote(int position, @NonNull Note note) {
        mNotes.set(position, note);
        notifyItemChanged(position);
    }

    void deleteNote(int itemPosition) {
        mNotes.remove(itemPosition);
        notifyItemRemoved(itemPosition);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.note_title)
        TextView noteTitle;

        @BindView(R.id.note_text)
        TextView noteText;

        @BindView(R.id.note_date)
        TextView noteDate;

        NoteHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}