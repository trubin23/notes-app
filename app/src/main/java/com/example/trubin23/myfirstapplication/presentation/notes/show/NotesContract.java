package com.example.trubin23.myfirstapplication.presentation.notes.show;

import com.example.trubin23.myfirstapplication.presentation.common.BaseView;

/**
 * Created by trubin23 on 28.12.17.
 */

public class NotesContract {

    interface View extends BaseView {

    }

    interface Presenter {
        void notesSync();
        void deleteNote();
    }
}
