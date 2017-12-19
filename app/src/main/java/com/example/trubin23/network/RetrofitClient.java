package com.example.trubin23.network;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by trubin23 on 18.12.17.
 */

public class RetrofitClient {

    private static final String BASE_URL = "http://notes.mrdekk.ru";

    private static Retrofit mRetrofit = null;

    private static Retrofit getClient(String baseUrl) {
        if (mRetrofit ==null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    private static SOService getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }

    public static void getNotes(Callback<List<Note>> callback){
        SOService mService = getSOService();
        mService.getNotes().enqueue(callback);
    }

    public static void getNote(String uid, Callback<Note> callback){
        SOService mService = getSOService();
        mService.getNote(uid).enqueue(callback);
    }

    public static void addNote(Note note, Callback<Note> callback){
        SOService mService = getSOService();
        mService.addNote(note).enqueue(callback);
    }

    public static void updateNote(String uid, Note note, Callback<Note> callback){
        SOService mService = getSOService();
        mService.updateNote(uid, note).enqueue(callback);
    }

    public static void deleteNote(String uid, Callback<Note> callback){
        SOService mService = getSOService();
        mService.deleteNote(uid).enqueue(callback);
    }
}
