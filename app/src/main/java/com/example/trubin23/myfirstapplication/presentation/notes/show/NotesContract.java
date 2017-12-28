package com.example.trubin23.myfirstapplication.presentation.notes.show;

import android.support.annotation.NonNull;

import com.example.trubin23.myfirstapplication.presentation.common.BaseView;

/**
 * Created by trubin23 on 28.12.17.
 */

public class NotesContract {

    interface View extends BaseView {
        void showSuccessToast(int eventId);
        void showErrorToast(int eventId);
    }

    interface Presenter {
        void notesSync();
        void deleteNote(@NonNull String noteUid);
    }
}
