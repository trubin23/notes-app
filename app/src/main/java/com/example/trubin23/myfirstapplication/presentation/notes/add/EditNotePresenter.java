package com.example.trubin23.myfirstapplication.presentation.notes.add;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.trubin23.myfirstapplication.R;
import com.example.trubin23.myfirstapplication.domain.common.BaseUseCase;
import com.example.trubin23.myfirstapplication.domain.common.UseCaseHandler;
import com.example.trubin23.myfirstapplication.domain.notes.model.NoteDomain;
import com.example.trubin23.myfirstapplication.domain.notes.usecase.AddNoteUseCase;
import com.example.trubin23.myfirstapplication.domain.notes.usecase.UpdateNoteUseCase;
import com.example.trubin23.myfirstapplication.presentation.common.BasePresenter;
import com.example.trubin23.myfirstapplication.presentation.notes.model.NoteView;
import com.example.trubin23.myfirstapplication.presentation.notes.model.NoteViewMapper;

/**
 * Created by trubin23 on 27.12.17.
 */

class EditNotePresenter extends BasePresenter<EditNoteContract.View> implements EditNoteContract.Presenter {

    private static final String TAG = EditNotePresenter.class.getSimpleName();

    private final AddNoteUseCase mAddNoteUseCase;
    private final UpdateNoteUseCase mUpdateNoteUseCase;

    EditNotePresenter(@NonNull UseCaseHandler useCaseHandler,
                      @NonNull AddNoteUseCase addNoteUseCase,
                      @NonNull UpdateNoteUseCase updateNoteUseCase) {
        super(useCaseHandler);
        mAddNoteUseCase = addNoteUseCase;
        mUpdateNoteUseCase = updateNoteUseCase;
    }

    @Override
    public void saveNote(NoteView noteView, boolean addNote) {
        if (addNote) {
            addNote(noteView);
        } else {
            updateNote(noteView);
        }
    }

    private void addNote(NoteView noteView) {
        NoteDomain noteDomain = NoteViewMapper.toDomainModel(noteView);
        mUseCaseHandler.execute(mAddNoteUseCase, new AddNoteUseCase.RequestValues(noteDomain),
                new BaseUseCase.UseCaseCallback<AddNoteUseCase.ResponseValues>() {

                    @Override
                    public void onSuccess(AddNoteUseCase.ResponseValues response) {
                        getView().showSuccessToast(R.string.note_added);
                        getView().savingInDb();
                    }

                    @Override
                    public void onError() {
                        getView().showErrorToast(R.string.note_added);
                        getView().savingInDb();
                        Log.e(TAG, "noteDao.addNote");
                    }
                });
    }

    private void updateNote(NoteView noteView) {
        NoteDomain noteDomain = NoteViewMapper.toDomainModel(noteView);
        mUseCaseHandler.execute(mUpdateNoteUseCase, new UpdateNoteUseCase.RequestValues(noteDomain),
                new BaseUseCase.UseCaseCallback<UpdateNoteUseCase.ResponseValues>() {

                    @Override
                    public void onSuccess(UpdateNoteUseCase.ResponseValues response) {
                        getView().showSuccessToast(R.string.note_updated);
                        getView().savingInDb();
                    }

                    @Override
                    public void onError() {
                        getView().showErrorToast(R.string.note_updated);
                        getView().savingInDb();
                        Log.e(TAG, "noteDao.updateNote");
                    }
                });
    }
}
