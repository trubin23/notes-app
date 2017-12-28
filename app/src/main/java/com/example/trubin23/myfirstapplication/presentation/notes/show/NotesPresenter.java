package com.example.trubin23.myfirstapplication.presentation.notes.show;

import android.support.annotation.NonNull;

import com.example.trubin23.myfirstapplication.domain.common.UseCaseHandler;
import com.example.trubin23.myfirstapplication.presentation.common.BasePresenter;

/**
 * Created by trubin23 on 28.12.17.
 */

public class NotesPresenter extends BasePresenter<NotesContract.View> implements NotesContract.Presenter {

    public NotesPresenter(@NonNull UseCaseHandler useCaseHandler) {
        super(useCaseHandler);
    }

    @Override
    public void notesSync() {

    }

    @Override
    public void deleteNote() {

    }
}
