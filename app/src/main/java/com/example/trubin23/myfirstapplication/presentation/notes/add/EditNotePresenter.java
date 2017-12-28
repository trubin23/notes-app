package com.example.trubin23.myfirstapplication.presentation.notes.add;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.trubin23.myfirstapplication.R;
import com.example.trubin23.myfirstapplication.domain.common.BaseUseCase;
import com.example.trubin23.myfirstapplication.domain.common.UseCaseHandler;
import com.example.trubin23.myfirstapplication.domain.notes.model.NoteDomain;
import com.example.trubin23.myfirstapplication.domain.notes.usecase.AddNoteUseCase;
import com.example.trubin23.myfirstapplication.presentation.common.BasePresenter;
import com.example.trubin23.myfirstapplication.presentation.notes.model.NoteView;
import com.example.trubin23.myfirstapplication.presentation.notes.model.NoteViewMapper;

/**
 * Created by trubin23 on 27.12.17.
 */

class EditNotePresenter extends BasePresenter<EditNoteContract.View> implements EditNoteContract.Presenter {

    private static final String TAG = EditNotePresenter.class.getSimpleName();

    private final AddNoteUseCase mAddNoteUseCase;

    EditNotePresenter(@NonNull UseCaseHandler useCaseHandler,
                             @NonNull AddNoteUseCase saveNoteUseCase) {
        super(useCaseHandler);
        mAddNoteUseCase = saveNoteUseCase;
    }

    @Override
    public void saveNote(NoteView noteView, boolean addNote) {
        if (addNote) {
            NoteDomain noteDomain = NoteViewMapper.toDomainModel(noteView);
            mUseCaseHandler.execute(mAddNoteUseCase, new AddNoteUseCase.RequestValues(noteDomain),
                                    new BaseUseCase.UseCaseCallback<AddNoteUseCase.ResponseValues>(){

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
        } else {
            //RetrofitClient.updateNote(noteView, updateProcessing());
        }
    }
/*

    @NonNull
    private ResponseProcessing<NoteStorage> updateProcessing() {
        return new ResponseProcessing<NoteStorage>() {

            @Override
            public void success(NoteStorage noteStorage) {
                final NoteDao noteDao = ((MyCustomApplication) getApplication()).getNoteDao();

                Completable.fromAction(() -> noteDao.updateNote(noteStorage))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    final LocalBroadcastManager broadcastManager =
                                            LocalBroadcastManager.getInstance(getApplicationContext());
                                    broadcastManager.sendBroadcast(new Intent(NotesActivity.ACTION_CHANGED_DB));
                                    getView().showSuccessToast(R.string.note_updated);
                                },
                                throwable -> Log.e(TAG, "noteDao.updateNote", throwable));
            }

            @Override
            public void error(RestError restError) {
                super.error(restError);
                getView().showErrorToast(R.string.note_updated, restError.getCode());
            }
        };
    }*/
}
