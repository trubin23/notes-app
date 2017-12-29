package com.example.trubin23.myfirstapplication.domain.notes.usecase;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.trubin23.myfirstapplication.domain.common.BaseUseCase;
import com.example.trubin23.myfirstapplication.storage.repository.NotesRepository;

/**
 * Created by trubin23 on 29.12.17.
 */

public class NetworkSyncNotesUseCase extends BaseUseCase<NetworkSyncNotesUseCase.RequestValues,
        NetworkSyncNotesUseCase.ResponseValues> {

    @Override
    protected void executeUseCase(NetworkSyncNotesUseCase.RequestValues requestValues) {
        Cursor cursor = NotesRepository.networkSyncNotes();

        if (cursor != null){
            getUseCaseCallback().onSuccess(new NetworkSyncNotesUseCase.ResponseValues(cursor));
        } else {
            getUseCaseCallback().onError();
        }
    }

    public static class RequestValues implements BaseUseCase.RequestValues {

    }

    public static class ResponseValues implements BaseUseCase.ResponseValues {
        private Cursor mCursor;

        ResponseValues(@NonNull Cursor cursor) {
            mCursor = cursor;
        }

        @NonNull
        public Cursor getCursor() {
            return mCursor;
        }
    }
}
