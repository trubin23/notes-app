package com.example.trubin23.myfirstapplication.presentation.notes.show.notelist;

import android.support.annotation.NonNull;

/**
 * Created by trubin23 on 13.12.17.
 */

public interface NoteItemActionHandler {
    void onEdit(@NonNull String uid);

    void onDelete(@NonNull String uid, @NonNull String noteTitle, int position);
}
