package com.example.trubin23.myfirstapplication.presentation.notes.add;

import com.example.trubin23.myfirstapplication.presentation.common.BaseView;
import com.example.trubin23.myfirstapplication.storage.model.Note;

/**
 * Created by trubin23 on 27.12.17.
 */

public interface EditNoteContract {

    interface View extends BaseView {
        void showSuccessToast(int eventId);
        void showErrorToast(int eventId, int errorCode);
    }

    interface Presenter {
        void saveNote(Note note, boolean addNote);
    }
}
