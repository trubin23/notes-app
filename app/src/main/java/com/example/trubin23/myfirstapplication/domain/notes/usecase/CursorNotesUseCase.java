package com.example.trubin23.myfirstapplication.domain.notes.usecase;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.trubin23.myfirstapplication.domain.common.BaseUseCase;
import com.example.trubin23.myfirstapplication.storage.repository.NotesRepository;

/**
 * Created by trubin23 on 28.12.17.
 */

public class CursorNotesUseCase extends BaseUseCase<CursorNotesUseCase.RequestValues, CursorNotesUseCase.ResponseValues> {

    @Override
    protected void executeUseCase(CursorNotesUseCase.RequestValues requestValues) {
        Cursor cursor = NotesRepository.getCursorAllData();

        if (cursor != null){
            getUseCaseCallback().onSuccess(new CursorNotesUseCase.ResponseValues(cursor));
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
