package com.example.trubin23.myfirstapplication.domain.notes.model;

import com.example.trubin23.myfirstapplication.storage.model.NoteStorage;

public class NoteDomainMapper {

    public static NoteStorage toStorageModel(NoteDomain noteDomain) {
        return new NoteStorage(noteDomain.getUid(), noteDomain.getTitle(),
                               noteDomain.getContent(), noteDomain.getColor(), null);
    }

    public static NoteDomain toDomainModel(NoteStorage noteStorage) {
        return new NoteDomain(noteStorage.getUid(), noteStorage.getTitle(), noteStorage.getContent(), noteStorage.getColor());
    }
}
