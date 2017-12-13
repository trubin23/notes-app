package com.example.trubin23.myfirstapplication;

import android.support.annotation.NonNull;

/**
 * Created by trubin23 on 13.12.17.
 */

public interface NoteItemActionHandler {
    void onEdit(@NonNull Note note);
    void onDelete(@NonNull Note note, int position);
}
