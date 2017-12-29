package com.example.trubin23.myfirstapplication.presentation.notes.show;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.trubin23.myfirstapplication.presentation.common.BaseView;

/**
 * Created by trubin23 on 28.12.17.
 */

public class NotesContract {

    interface View extends BaseView {
        void refreshRecyclerView(Cursor cursor);
        void stopSwipeRefresh();
        void showSuccessToast(int eventId);
        void showErrorToast(int eventId);
    }

    interface Presenter {
        void notesWithNetworkSync();
        void reloadNotesFromDb();
        void deleteNote(@NonNull String noteUid);
    }
}
