package com.example.trubin23.myfirstapplication.storage.repository;

import com.example.trubin23.myfirstapplication.storage.database.DatabaseHelper;
import com.example.trubin23.myfirstapplication.storage.database.NoteDao;
import com.example.trubin23.myfirstapplication.storage.database.NoteDaoImpl;
import com.example.trubin23.myfirstapplication.storage.model.Note;
import com.example.trubin23.myfirstapplication.storage.network.RetrofitClient;

import retrofit2.Response;

/**
 * Created by trubin23 on 27.12.17.
 */

public class NotesRepository {

    public static boolean saveNote(Note note) {
        Response<Note> noteResponse = RetrofitClient.addNoteSync(note);
        if (noteResponse == null)
            return false;

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        if (databaseHelper != null) {
            NoteDao noteDao = new NoteDaoImpl(databaseHelper);
            noteDao.addNote(note);
        }

        return true;
    }
}
