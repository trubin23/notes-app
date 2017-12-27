package com.example.trubin23.myfirstapplication.domain.notes.model;

import com.example.trubin23.myfirstapplication.storage.model.Note;

public class NoteDomainModelMapper {

    public static Note toDataModel(NoteDomainModel noteDomainModel) {
        return new Note(noteDomainModel.getUid(), noteDomainModel.getTitle(),
                noteDomainModel.getContent(), noteDomainModel.getColor(), null);
    }

    public static NoteDomainModel toDomainModel(Note note) {
        return new NoteDomainModel(note.getUid(), note.getTitle(), note.getContent(), note.getColor());
    }
}
