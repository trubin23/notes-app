package com.example.trubin23.myfirstapplication.domain.notes.usecase;

import android.support.annotation.NonNull;

import com.example.trubin23.myfirstapplication.domain.common.BaseUseCase;
import com.example.trubin23.myfirstapplication.domain.notes.model.NoteDomain;
import com.example.trubin23.myfirstapplication.domain.notes.model.NoteDomainMapper;
import com.example.trubin23.myfirstapplication.storage.model.NoteStorage;
import com.example.trubin23.myfirstapplication.storage.repository.NotesRepository;

/**
 * Created by trubin23 on 28.12.17.
 */

public class LoadNoteUseCase extends BaseUseCase<LoadNoteUseCase.RequestValues, LoadNoteUseCase.ResponseValues> {

    @Override
    protected void executeUseCase(LoadNoteUseCase.RequestValues requestValues) {
        String noteUid = requestValues.getNoteUid();

        NoteStorage noteStorage = NotesRepository.loadNote(noteUid);

        NoteDomain noteDomain = NoteDomainMapper.toDomainModel(noteStorage);

        if (noteStorage != null){
            getUseCaseCallback().onSuccess(new LoadNoteUseCase.ResponseValues(noteDomain));
        } else {
            getUseCaseCallback().onError();
        }
    }

    public static class RequestValues implements BaseUseCase.RequestValues {
        private String mNoteUid;

        public RequestValues(@NonNull String noteUid) {
            mNoteUid = noteUid;
        }

        @NonNull
        String getNoteUid() {
            return mNoteUid;
        }
    }

    public static class ResponseValues implements BaseUseCase.ResponseValues {
        private NoteDomain mNoteDomain;

        ResponseValues(@NonNull NoteDomain noteDomain) {
            mNoteDomain = noteDomain;
        }

        @NonNull
        public NoteDomain getNoteDomainModel() {
            return mNoteDomain;
        }
    }
}
