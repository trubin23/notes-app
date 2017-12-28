package com.example.trubin23.myfirstapplication.domain.notes.usecase;

import android.support.annotation.NonNull;

import com.example.trubin23.myfirstapplication.domain.common.BaseUseCase;
import com.example.trubin23.myfirstapplication.domain.notes.model.NoteDomain;
import com.example.trubin23.myfirstapplication.domain.notes.model.NoteDomainMapper;
import com.example.trubin23.myfirstapplication.storage.model.NoteStorage;
import com.example.trubin23.myfirstapplication.storage.repository.NotesRepository;

public class UpdateNoteUseCase extends BaseUseCase<UpdateNoteUseCase.RequestValues, UpdateNoteUseCase.ResponseValues> {

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        NoteDomain noteDomain = requestValues.getNoteDomainModel();

        NoteStorage noteStorage = NoteDomainMapper.toStorageModel(noteDomain);

        boolean result = NotesRepository.updateNote(noteStorage);

        if (result){
            getUseCaseCallback().onSuccess(new ResponseValues());
        } else {
            getUseCaseCallback().onError();
        }
    }

    public static class RequestValues implements BaseUseCase.RequestValues {

        private NoteDomain mNoteDomain;

        public RequestValues(@NonNull NoteDomain noteDomain) {
            mNoteDomain = noteDomain;
        }

        @NonNull
        NoteDomain getNoteDomainModel() {
            return mNoteDomain;
        }
    }

    public static class ResponseValues implements BaseUseCase.ResponseValues {

    }
}
