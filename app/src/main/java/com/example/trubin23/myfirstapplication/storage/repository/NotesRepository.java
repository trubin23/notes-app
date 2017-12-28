package com.example.trubin23.myfirstapplication.storage.repository;

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

    public static boolean saveNote(NoteStorage noteStorage) {
        Response<NoteStorage> noteResponse = RetrofitClient.addNoteSync(noteStorage);
        if (noteResponse == null)
            return false;

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        if (databaseHelper != null) {
            NoteDao noteDao = new NoteDaoImpl(databaseHelper);
            noteDao.addNote(noteStorage);
        }

        return true;
    }
}
