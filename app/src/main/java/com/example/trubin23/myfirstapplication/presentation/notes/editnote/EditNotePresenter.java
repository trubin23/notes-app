package com.example.trubin23.myfirstapplication.presentation.notes.editnote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.trubin23.myfirstapplication.R;
import com.example.trubin23.myfirstapplication.domain.common.BaseUseCase;
import com.example.trubin23.myfirstapplication.domain.common.UseCaseHandler;
import com.example.trubin23.myfirstapplication.domain.notes.model.NoteDomain;
import com.example.trubin23.myfirstapplication.domain.notes.usecase.AddNoteUseCase;
import com.example.trubin23.myfirstapplication.domain.notes.usecase.GetNoteUseCase;
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
    private final GetNoteUseCase mGetNoteUseCase;

    EditNotePresenter(@NonNull UseCaseHandler useCaseHandler,
                      @NonNull AddNoteUseCase addNoteUseCase,
                      @NonNull UpdateNoteUseCase updateNoteUseCase,
                      @NonNull GetNoteUseCase getNoteUseCase) {
        super(useCaseHandler);
        mAddNoteUseCase = addNoteUseCase;
        mUpdateNoteUseCase = updateNoteUseCase;
        mGetNoteUseCase = getNoteUseCase;
    }

    @Override
    public void saveNote(@NonNull NoteView noteView, boolean addNote) {
        if (addNote) {
            addNote(noteView);
        } else {
            updateNote(noteView);
        }
    }

    private void addNote(@NonNull NoteView noteView) {
        NoteDomain noteDomain = NoteViewMapper.toDomainModel(noteView);
        mUseCaseHandler.execute(mAddNoteUseCase, new AddNoteUseCase.RequestValues(noteDomain),
                new BaseUseCase.UseCaseCallback<AddNoteUseCase.ResponseValues>() {

                    @Override
                    public void onSuccess(AddNoteUseCase.ResponseValues response) {
                        getView().showSuccessToast(R.string.note_added);
                        getView().savingDbComplete();
                    }

                    @Override
                    public void onError() {
                        getView().showErrorToast(R.string.note_added);
                        getView().savingDbComplete();
                        Log.e(TAG, "noteDao.addNote");
                    }
                });
    }

    private void updateNote(@NonNull NoteView noteView) {
        NoteDomain noteDomain = NoteViewMapper.toDomainModel(noteView);
        mUseCaseHandler.execute(mUpdateNoteUseCase, new UpdateNoteUseCase.RequestValues(noteDomain),
                new BaseUseCase.UseCaseCallback<UpdateNoteUseCase.ResponseValues>() {

                    @Override
                    public void onSuccess(UpdateNoteUseCase.ResponseValues response) {
                        getView().showSuccessToast(R.string.note_updated);
                        getView().savingDbComplete();
                    }

                    @Override
                    public void onError() {
                        getView().showErrorToast(R.string.note_updated);
                        getView().savingDbComplete();
                        Log.e(TAG, "noteDao.updateNote");
                    }
                });
    }

    @Override
    public void loadNote(@NonNull String noteUid) {
        mUseCaseHandler.execute(mGetNoteUseCase, new GetNoteUseCase.RequestValues(noteUid),
                new BaseUseCase.UseCaseCallback<GetNoteUseCase.ResponseValues>() {

                    @Override
                    public void onSuccess(GetNoteUseCase.ResponseValues response) {
                        NoteDomain noteDomain = response.getNoteDomainModel();
                        NoteView noteView = NoteViewMapper.toViewModel(noteDomain);

                        getView().showSuccessToast(R.string.note_loaded);
                        getView().setNote(noteView);
                    }

                    @Override
                    public void onError() {
                        getView().showErrorToast(R.string.note_loaded);
                        Log.e(TAG, "noteDao.note_loaded");
                    }
                });
    }
}
