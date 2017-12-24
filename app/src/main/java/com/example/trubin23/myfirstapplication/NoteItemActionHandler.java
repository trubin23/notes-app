package com.example.trubin23.myfirstapplication;

import android.support.annotation.NonNull;

/**
 * Created by trubin23 on 13.12.17.
 */

public interface NoteItemActionHandler {
    void onEdit(@NonNull String uid);
    void onDelete(@NonNull String uid, String noteTitle, int position);
}
