package com.example.trubin23.json;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.trubin23.myfirstapplication.Note;
import com.example.trubin23.myfirstapplication.R;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by trubin23 on 10.12.17.
 */

public class InitializeData {

    private static final String LOG = "DBNotes";

    @NonNull
    public static List<Note> initializeData(@NonNull Context context) {
        String json = "";

        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.notes);

            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            json = new String(b);
        } catch (IOException e) {
            Log.e(LOG, "List<Note> initializeData(@NonNull Context context)", e);
        }

        return jsonToListNotes(json);
    }

    @NonNull
    private static List<Note> jsonToListNotes(@NonNull String json) {
        Moshi moshi = new Moshi.Builder().build();

        Type listOfCardsType = Types.newParameterizedType(List.class, NoteParse.class);
        JsonAdapter<List<NoteParse>> jsonAdapter = moshi.adapter(listOfCardsType);

        List<Note> notes = new ArrayList<>();
        try {
            List<NoteParse> notesParse = jsonAdapter.fromJson(json);
            if (notesParse != null) {
                for (NoteParse noteParse : notesParse) {
                    notes.add(new Note(noteParse.title, noteParse.text, noteParse.date));
                }
            }
        } catch (IOException e) {
            Log.e(LOG, "List<Note> initializeData()", e);
        }

        return notes;
    }
}
