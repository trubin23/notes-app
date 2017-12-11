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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by trubin23 on 29.11.17.
 */

public class RecyclerNoteAdapter extends RecyclerView.Adapter<RecyclerNoteAdapter.NoteHolder>{

    private List<Note> mNotes;
    private View.OnClickListener mOnClickEditNote;
    private View.OnLongClickListener mOnLongClickDeleteNote;

    RecyclerNoteAdapter(@Nullable List<Note> notes, View.OnClickListener onClickEditNote,
                        View.OnLongClickListener onLongClickDeleteNote){
        setNotes(notes);
        this.mOnClickEditNote = onClickEditNote;
        this.mOnLongClickDeleteNote = onLongClickDeleteNote;
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new NoteHolder(v);
    }

    @Override
    public void onBindViewHolder(NoteHolder holder, int position) {
        Note note = mNotes.get(position);

        holder.noteTitle.setText(note.getTitle());
        holder.noteText.setText(note.getText());
        holder.noteDate.setText(note.getDate());

        holder.itemView.setOnClickListener(mOnClickEditNote);
        holder.itemView.setOnLongClickListener(mOnLongClickDeleteNote);
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    @Nullable
    Note getItem(int position){
        return mNotes.get(position);
    }

    @NonNull
    List<Note> getNotes(){
        return mNotes;
    }

    /**
     * Sets a new list of notes in the RecyclerView
     * @param notes note.getId() must not be equal to Note.DEFAULT_ID
     */
    void setNotes(@Nullable List<Note> notes) {
        mNotes = new ArrayList<>();

        if (notes != null) {
            for (Note note : notes) {
                if (note.getId() != DatabaseHelper.DEFAULT_ID) {
                    mNotes.add(note);
                }
            }
        }

        notifyDataSetChanged();
    }

    boolean addNote(@NonNull Note note){
        if (note.getId() != DatabaseHelper.DEFAULT_ID) {
            mNotes.add(note);
            notifyItemInserted(mNotes.size() - 1);

            return true;
        } else {
            return false;
        }
    }

    void updateNote(int position, @NonNull Note note){
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

        NoteHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}