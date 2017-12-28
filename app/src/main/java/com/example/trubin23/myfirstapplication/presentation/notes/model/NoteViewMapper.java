package com.example.trubin23.myfirstapplication.presentation.notes.model;

import android.support.annotation.NonNull;

import com.example.trubin23.myfirstapplication.domain.notes.model.NoteDomain;

/**
 * Created by Andrey on 27.12.2017.
 */

public class NoteViewMapper {

    @NonNull
    public static NoteDomain toDomainModel(@NonNull NoteView noteStorage) {
        return new NoteDomain(noteStorage.getUid(), noteStorage.getTitle(),
                              noteStorage.getContent(), noteStorage.getColor());
    }

    @NonNull
    public static NoteView toViewModel(@NonNull NoteDomain noteDomain) {
        return new NoteView(noteDomain.getUid(), noteDomain.getTitle(),
                               noteDomain.getContent(), noteDomain.getColor());
    }
}
