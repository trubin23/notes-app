package com.example.trubin23.myfirstapplication.presentation.notes.add;

import com.example.trubin23.myfirstapplication.presentation.common.BaseView;
import com.example.trubin23.myfirstapplication.presentation.notes.model.NoteView;
import com.example.trubin23.myfirstapplication.storage.model.NoteStorage;

/**
 * Created by trubin23 on 27.12.17.
 */

public interface EditNoteContract {

    interface View extends BaseView {
        void showSuccessToast(int eventId);
        void showErrorToast(int eventId);
        void savingInDb();
    }

    interface Presenter {
        void saveNote(NoteView noteView, boolean addNote);
    }
}
