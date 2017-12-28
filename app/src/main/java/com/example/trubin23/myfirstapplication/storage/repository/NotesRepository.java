package com.example.trubin23.myfirstapplication.storage.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.trubin23.myfirstapplication.storage.database.DatabaseHelper;
import com.example.trubin23.myfirstapplication.storage.database.NoteDao;
import com.example.trubin23.myfirstapplication.storage.database.NoteDaoImpl;
import com.example.trubin23.myfirstapplication.storage.model.NoteStorage;
import com.example.trubin23.myfirstapplication.storage.network.RetrofitClient;

import retrofit2.Response;

/**
 * Created by trubin23 on 27.12.17.
 */

public class NotesRepository {

    public static boolean addNote(@NonNull NoteStorage noteStorage) {
        Response<NoteStorage> noteResponse = RetrofitClient.addNoteSync(noteStorage);
        if (noteResponse == null) {
            return false;
        }

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        if (databaseHelper == null) {
            return false;
        }

        NoteDao noteDao = new NoteDaoImpl(databaseHelper);
        noteDao.addNote(noteStorage);
        return true;
    }

    public static boolean updateNote(@NonNull NoteStorage noteStorage) {
        Response<NoteStorage> noteResponse = RetrofitClient.updateNoteSync(noteStorage);
        if (noteResponse == null) {
            return false;
        }

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        if (databaseHelper == null) {
            return false;
        }

        NoteDao noteDao = new NoteDaoImpl(databaseHelper);
        noteDao.updateNote(noteStorage);
        return true;
    }

    public static boolean deleteNote(@NonNull String uid) {
        Response<NoteStorage> noteResponse = RetrofitClient.deleteNoteSync(uid);
        if (noteResponse == null) {
            return false;
        }

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        if (databaseHelper == null) {
            return false;
        }

        NoteDao noteDao = new NoteDaoImpl(databaseHelper);
        noteDao.deleteNote(uid);
        return true;
    }

    @Nullable
    public static NoteStorage loadNote(@NonNull String uid) {
        NoteStorage noteStorage = null;

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        if (databaseHelper != null) {
            NoteDao noteDao = new NoteDaoImpl(databaseHelper);
            noteStorage = noteDao.getNote(uid);
        }

        return noteStorage;
    }
}
