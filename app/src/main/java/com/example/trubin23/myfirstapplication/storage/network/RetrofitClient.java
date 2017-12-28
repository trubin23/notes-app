package com.example.trubin23.myfirstapplication.storage.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.trubin23.myfirstapplication.storage.model.NoteStorage;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by trubin23 on 18.12.17.
 */

public class RetrofitClient {

    private static final String TAG = "RetrofitClient";
    private static final String BASE_URL = "http://notes.mrdekk.ru";

    private static SOService mSOService = null;
    private static Converter<ResponseBody, RestError> mConverter = null;

    @NonNull
    private static SOService getSOService() {
        if (mSOService == null) {
            OkHttpClient httpClient = new OkHttpClient().newBuilder().addInterceptor(
                    chain -> {
                        Request request = chain.request();
                        Request newRequest = request.newBuilder()
                                .addHeader("Content-Type", "application/json; charset=utf-8")
                                .addHeader("Authorization", "Bearer Leonardo")
                                .build();

                        return chain.proceed(newRequest);
                    }
            ).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();

            mSOService = retrofit.create(SOService.class);

            mConverter = retrofit
                    .responseBodyConverter(RestError.class, new Annotation[0]);
        }
        return mSOService;
    }

    @Nullable
    public static RestError convertRestError(@Nullable ResponseBody errorBody) {
        if (mConverter != null && errorBody != null) {
            try {
                return mConverter.convert(errorBody);
            } catch (IOException e) {
                Log.e(TAG,
                        "public static RestError convertRestError(ResponseBody errorBody)", e);
                e.printStackTrace();
            }
        }

        return null;
    }

    public static void getNotes(@NonNull Callback<List<NoteStorage>> callback) {
        SOService mService = getSOService();
        mService.getNotes().enqueue(callback);
    }

    static void getNote(@NonNull String uid, @NonNull Callback<NoteStorage> callback) {
        SOService mService = getSOService();
        mService.getNote(uid).enqueue(callback);
    }

    public static void addNote(@NonNull NoteStorage noteStorage, @NonNull Callback<NoteStorage> callback) {
        SOService mService = getSOService();
        mService.addNote(noteStorage).enqueue(callback);
    }

    public static void updateNote(@NonNull NoteStorage noteStorage, @NonNull Callback<NoteStorage> callback) {
        SOService mService = getSOService();
        mService.updateNote(noteStorage.getUid(), noteStorage).enqueue(callback);
    }

    public static void deleteNote(@NonNull String uid, @NonNull Callback<NoteStorage> callback) {
        SOService mService = getSOService();
        mService.deleteNote(uid).enqueue(callback);
    }

    @Nullable
    public static Response<NoteStorage> addNoteSync(@NonNull NoteStorage noteStorage) {
        SOService mService = getSOService();

        Response<NoteStorage> noteResponse = null;
        try {
            noteResponse = mService.addNote(noteStorage).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return noteResponse;
    }

    @Nullable
    public static Response<NoteStorage> updateNoteSync(@NonNull NoteStorage noteStorage) {
        SOService mService = getSOService();

        Response<NoteStorage> noteResponse = null;
        try {
            noteResponse = mService.updateNote(noteStorage.getUid(), noteStorage).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return noteResponse;
    }

    @Nullable
    public static Response<NoteStorage> deleteNoteSync(@NonNull String noteUid) {
        SOService mService = getSOService();

        Response<NoteStorage> noteResponse = null;
        try {
            noteResponse = mService.deleteNote(noteUid).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return noteResponse;
    }
}
