package com.example.trubin23.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by trubin23 on 18.12.17.
 */

public interface SOService {

    @GET("/notes")
    @Headers({
            "Content-Type: application/json; charset=utf-8",
            "Authorization: Bearer 1234567890"
    })
    Call<List<Note>> getNotes();

    @GET("/notes/{uid}")
    @Headers({
            "Content-Type: application/json; charset=utf-8",
            "Authorization: Bearer 1234567890"
    })
    Call<Note> getNote(@Path("uid") String uid);


    @POST("/notes")
    @Headers({
            "Content-Type: application/json; charset=utf-8",
            "Authorization: Bearer 1234567890"
    })
    Call<Note> addNote(@Body Note note);

    @PUT("/notes/{uid}")
    @Headers({
            "Content-Type: application/json; charset=utf-8",
            "Authorization: Bearer 1234567890"
    })
    Call<Note> updateNote(@Path("uid") String uid, @Body Note note);

    @DELETE("/notes/{uid}")
    @Headers({
            "Content-Type: application/json; charset=utf-8",
            "Authorization: Bearer 1234567890"
    })
    Call<Note> deleteNote(@Path("uid") String uid);
}
