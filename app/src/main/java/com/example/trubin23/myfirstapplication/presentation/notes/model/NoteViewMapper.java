package com.example.trubin23.myfirstapplication.presentation.notes.model;

import com.example.trubin23.myfirstapplication.domain.notes.model.NoteDomain;
import com.example.trubin23.myfirstapplication.storage.model.NoteStorage;

/**
 * Created by Andrey on 27.12.2017.
 */

public class NoteViewMapper {

    public static NoteDomain toDomainModel(NoteView noteStorage) {
        return new NoteDomain(noteStorage.getUid(), noteStorage.getTitle(),
                              noteStorage.getContent(), noteStorage.getColor());
    }

    public static NoteView toViewModel(NoteDomain noteDomain) {
        return new NoteView(noteDomain.getUid(), noteDomain.getTitle(),
                               noteDomain.getContent(), noteDomain.getColor());
    }
}
