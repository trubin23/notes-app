package com.example.trubin23.myfirstapplication.presentation.notes.add;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.trubin23.myfirstapplication.R;
import com.example.trubin23.myfirstapplication.MyCustomApplication;
import com.example.trubin23.myfirstapplication.presentation.common.BasePresenter;
import com.example.trubin23.myfirstapplication.presentation.notes.show.NotesActivity;
import com.example.trubin23.myfirstapplication.storage.model.Note;
import com.example.trubin23.myfirstapplication.storage.database.NoteDao;
import com.example.trubin23.myfirstapplication.storage.network.ResponseProcessing;
import com.example.trubin23.myfirstapplication.storage.network.RestError;
import com.example.trubin23.myfirstapplication.storage.network.RetrofitClient;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by trubin23 on 27.12.17.
 */

class EditNotePresenter extends BasePresenter<EditNoteContract.View> implements EditNoteContract.Presenter {

    private static final String TAG = EditNotePresenter.class.getSimpleName();

    @Override
    public void saveNote(Note note, boolean addNote) {
        if (addNote) {
            //RetrofitClient.addNote(note, addProcessing());
            mUseCaseHandler.execute(mSaveNoteUseCase, new SaveNoteUseCase.RequestValues(noteDomainModel), new EmptyUseCaseCallback<SaveNoteUseCase.ResponseValues>());
        } else {
            RetrofitClient.updateNote(note, updateProcessing());
        }
    }

    @NonNull
    private ResponseProcessing<Note> addProcessing() {
        return new ResponseProcessing<Note>() {
            @Override
            public void success(Note note) {
                final NoteDao noteDao = ((MyCustomApplication) getApplication()).getNoteDao();

                Completable.fromAction(() -> noteDao.addNote(note))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                                    final LocalBroadcastManager broadcastManager =
                                            LocalBroadcastManager.getInstance(getApplicationContext());
                                    broadcastManager.sendBroadcast(new Intent(NotesActivity.ACTION_CHANGED_DB));
                                    getView().showSuccessToast(R.string.note_added);
                                },
                                throwable -> Log.e(TAG, "noteDao.addNote", throwable));
            }

            @Override
            public void error(RestError restError) {
                super.error(restError);
                getView().showErrorToast(R.string.note_added, restError.getCode());
            }
        };
    }

    @NonNull
    private ResponseProcessing<Note> updateProcessing() {
        return new ResponseProcessing<Note>() {

            @Override
            public void success(Note note) {
                final NoteDao noteDao = ((MyCustomApplication) getApplication()).getNoteDao();

                Completable.fromAction(() -> noteDao.updateNote(note))
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
    }
}
