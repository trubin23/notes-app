package com.example.trubin23.myfirstapplication.storage.network;

import com.example.trubin23.myfirstapplication.storage.model.NoteStorage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by trubin23 on 18.12.17.
 */

public interface SOService {

    @GET("/notes")
    Call<List<NoteStorage>> getNotes();

    @GET("/notes/{uid}")
    Call<NoteStorage> getNote(@Path("uid") String uid);

    @POST("/notes")
    Call<NoteStorage> addNote(@Body NoteStorage noteStorage);

    @PUT("/notes/{uid}")
    Call<NoteStorage> updateNote(@Path("uid") String uid, @Body NoteStorage noteStorage);

    @DELETE("/notes/{uid}")
    Call<NoteStorage> deleteNote(@Path("uid") String uid);
}
