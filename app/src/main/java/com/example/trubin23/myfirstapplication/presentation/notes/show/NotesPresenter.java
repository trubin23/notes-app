package com.example.trubin23.myfirstapplication.presentation.notes.show;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.trubin23.myfirstapplication.R;
import com.example.trubin23.myfirstapplication.domain.common.BaseUseCase;
import com.example.trubin23.myfirstapplication.domain.common.UseCaseHandler;
import com.example.trubin23.myfirstapplication.domain.notes.usecase.CursorNotesUseCase;
import com.example.trubin23.myfirstapplication.domain.notes.usecase.DeleteNoteUseCase;
import com.example.trubin23.myfirstapplication.domain.notes.usecase.NetworkSyncNotesUseCase;
import com.example.trubin23.myfirstapplication.presentation.common.BasePresenter;

/**
 * Created by trubin23 on 28.12.17.
 */

public class NotesPresenter extends BasePresenter<NotesContract.View> implements NotesContract.Presenter {

    private static final String TAG = NotesPresenter.class.getSimpleName();

    private final DeleteNoteUseCase mDeleteNoteUseCase;
    private final CursorNotesUseCase mCursorNotesUseCase;
    private final NetworkSyncNotesUseCase mNetworkSyncNotesUseCase;

    NotesPresenter(@NonNull UseCaseHandler useCaseHandler,
                   @NonNull DeleteNoteUseCase deleteNoteUseCase,
                   @NonNull CursorNotesUseCase cursorNotesUseCase,
                   @NonNull NetworkSyncNotesUseCase networkSyncNotesUseCase) {
        super(useCaseHandler);
        mDeleteNoteUseCase = deleteNoteUseCase;
        mCursorNotesUseCase = cursorNotesUseCase;
        mNetworkSyncNotesUseCase = networkSyncNotesUseCase;
    }

    @Override
    public void notesWithNetworkSync() {
        mUseCaseHandler.execute(mNetworkSyncNotesUseCase, new NetworkSyncNotesUseCase.RequestValues(),
                new BaseUseCase.UseCaseCallback<NetworkSyncNotesUseCase.ResponseValues>() {
                    @Override
                    public void onSuccess(NetworkSyncNotesUseCase.ResponseValues response) {
                        getView().refreshRecyclerView(response.getCursor());
                    }

                    @Override
                    public void onError() {
                        getView().showErrorToast(R.string.note_added);
                        getView().stopSwipeRefresh();
                        Log.e(TAG, "noteDao.addNote");
                    }
                });
    }

    @Override
    public void reloadNotesFromDb(){
        mUseCaseHandler.execute(mCursorNotesUseCase, new CursorNotesUseCase.RequestValues(),
                new BaseUseCase.UseCaseCallback<CursorNotesUseCase.ResponseValues>() {
                    @Override
                    public void onSuccess(CursorNotesUseCase.ResponseValues response) {
                        getView().refreshRecyclerView(response.getCursor());
                    }

                    @Override
                    public void onError() {
                        getView().showErrorToast(R.string.note_added);
                        Log.e(TAG, "noteDao.addNote");
                    }
                });
    }

    @Override
    public void deleteNote(@NonNull String noteUid) {
        mUseCaseHandler.execute(mDeleteNoteUseCase, new DeleteNoteUseCase.RequestValues(noteUid),
                new BaseUseCase.UseCaseCallback<DeleteNoteUseCase.ResponseValues>() {

                    @Override
                    public void onSuccess(DeleteNoteUseCase.ResponseValues response) {
                        getView().showSuccessToast(R.string.note_added);
                        //notesSync();
                    }

                    @Override
                    public void onError() {
                        getView().showErrorToast(R.string.note_added);
                        Log.e(TAG, "noteDao.addNote");
                    }
                });
    }
}
