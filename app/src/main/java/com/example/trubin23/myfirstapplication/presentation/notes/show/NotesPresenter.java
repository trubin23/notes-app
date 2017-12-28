package com.example.trubin23.myfirstapplication.presentation.notes.show;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.trubin23.myfirstapplication.R;
import com.example.trubin23.myfirstapplication.domain.common.BaseUseCase;
import com.example.trubin23.myfirstapplication.domain.common.UseCaseHandler;
import com.example.trubin23.myfirstapplication.domain.notes.usecase.DeleteNoteUseCase;
import com.example.trubin23.myfirstapplication.presentation.common.BasePresenter;

/**
 * Created by trubin23 on 28.12.17.
 */

public class NotesPresenter extends BasePresenter<NotesContract.View> implements NotesContract.Presenter {

    private static final String TAG = NotesPresenter.class.getSimpleName();

    private final DeleteNoteUseCase mDeleteNoteUseCase;

    NotesPresenter(@NonNull UseCaseHandler useCaseHandler,
                   @NonNull DeleteNoteUseCase deleteNoteUseCase) {
        super(useCaseHandler);
        mDeleteNoteUseCase = deleteNoteUseCase;
    }

    @Override
    public void notesSync() {

    }

    @Override
    public void deleteNote(@NonNull String noteUid) {
        mUseCaseHandler.execute(mDeleteNoteUseCase, new DeleteNoteUseCase.RequestValues(noteUid),
                new BaseUseCase.UseCaseCallback<DeleteNoteUseCase.ResponseValues>() {

                    @Override
                    public void onSuccess(DeleteNoteUseCase.ResponseValues response) {
                        getView().showSuccessToast(R.string.note_added);
                        notesSync();
                    }

                    @Override
                    public void onError() {
                        getView().showErrorToast(R.string.note_added);
                        Log.e(TAG, "noteDao.addNote");
                    }
                });
    }
}
