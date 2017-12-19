package com.example.trubin23.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by trubin23 on 18.12.17.
 */

public class RetrofitClient {

    private static final String TAG = "RetrofitClient";
    private static final String BASE_URL = "http://notes.mrdekk.ru";

    private static Retrofit mRetrofit = null;
    private static Converter<ResponseBody, RestError> mConverter = null;

    private static Retrofit getClient(String baseUrl) {
        if (mRetrofit ==null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();

            mConverter = mRetrofit
                    .responseBodyConverter(RestError.class, new Annotation[0]);
        }
        return mRetrofit;
    }

    private static SOService getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }

    @Nullable
    public static RestError convertRestError(@Nullable ResponseBody errorBody){
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

    public static void getNotes(@NonNull Callback<List<Note>> callback){
        SOService mService = getSOService();
        mService.getNotes().enqueue(callback);
    }

    public static void getNote(@NonNull String uid, @NonNull Callback<Note> callback){
        SOService mService = getSOService();
        mService.getNote(uid).enqueue(callback);
    }

    public static void addNote(@NonNull Note note, @NonNull Callback<Note> callback){
        SOService mService = getSOService();
        mService.addNote(note).enqueue(callback);
    }

    public static void updateNote(@NonNull String uid,
                                  @NonNull Note note, @NonNull Callback<Note> callback){
        SOService mService = getSOService();
        mService.updateNote(uid, note).enqueue(callback);
    }

    public static void deleteNote(@NonNull String uid, @NonNull Callback<Note> callback){
        SOService mService = getSOService();
        mService.deleteNote(uid).enqueue(callback);
    }
}