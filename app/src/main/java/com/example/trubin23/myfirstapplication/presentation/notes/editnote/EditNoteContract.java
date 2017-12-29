package com.example.trubin23.myfirstapplication.presentation.notes.editnote;

import android.support.annotation.NonNull;

import com.example.trubin23.myfirstapplication.presentation.common.BaseView;
import com.example.trubin23.myfirstapplication.presentation.notes.model.NoteView;

/**
 * Created by trubin23 on 27.12.17.
 */

public interface EditNoteContract {

    interface View extends BaseView {
        void showSuccessToast(int eventId);
        void showErrorToast(int eventId);
        void savingDbComplete();
        void setNote(@NonNull NoteView noteView);
    }

    interface Presenter {
        void saveNote(@NonNull NoteView noteView, boolean addNote);
        void loadNote(@NonNull String noteUid);
    }
}
