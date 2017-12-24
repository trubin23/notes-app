package com.example.trubin23.json;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.trubin23.database.Note;
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

    private static final String LOG = "InitializeData";

    @NonNull
    public static List<Note> initializeData(@NonNull Context context) {
        String json = "";

        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.notes);

            byte[] bytes = new byte[inputStream.available()];
            //noinspection ResultOfMethodCallIgnored
            inputStream.read(bytes);
            json = new String(bytes);
        } catch (IOException e) {
            Log.e(LOG, "List<Note> initializeData(@NonNull Context context)", e);
        }

        return jsonToListNotes(json);
    }

    @NonNull
    private static List<Note> jsonToListNotes(@NonNull String json) {
        Moshi moshi = new Moshi.Builder().build();

        Type listOfCardsType = Types.newParameterizedType(List.class, Note.class);
        JsonAdapter<List<Note>> jsonAdapter = moshi.adapter(listOfCardsType);

        try {
            List<Note> noteList = jsonAdapter.fromJson(json);
            if (noteList != null) {
                return noteList;
            }
        } catch (IOException e) {
            Log.e(LOG, "private static List<Note> jsonToListNotes(@NonNull String json)", e);
        }

        return new ArrayList<>();
    }
}
