package com.example.trubin23.myfirstapplication.domain.notes.usecase;

import android.support.annotation.NonNull;

import com.example.trubin23.myfirstapplication.domain.common.BaseUseCase;
import com.example.trubin23.myfirstapplication.storage.repository.NotesRepository;

/**
 * Created by trubin23 on 28.12.17.
 */

public class DeleteNoteUseCase extends BaseUseCase<DeleteNoteUseCase.RequestValues, DeleteNoteUseCase.ResponseValues> {

    @Override
    protected void executeUseCase(@NonNull RequestValues requestValues) {
        String mNoteUid = requestValues.getNoteUid();

        boolean result = NotesRepository.deleteNote(mNoteUid);

        if (result){
            getUseCaseCallback().onSuccess(new DeleteNoteUseCase.ResponseValues());
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

    }
}
