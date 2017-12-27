package com.example.trubin23.myfirstapplication.storage.network;

import com.example.trubin23.myfirstapplication.storage.model.Note;

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
    Call<List<Note>> getNotes();

    @GET("/notes/{uid}")
    Call<Note> getNote(@Path("uid") String uid);

    @POST("/notes")
    Call<Note> addNote(@Body Note note);

    @PUT("/notes/{uid}")
    Call<Note> updateNote(@Path("uid") String uid, @Body Note note);

    @DELETE("/notes/{uid}")
    Call<Note> deleteNote(@Path("uid") String uid);
}
