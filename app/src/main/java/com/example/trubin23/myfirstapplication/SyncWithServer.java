package com.example.trubin23.myfirstapplication;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.example.trubin23.database.NoteDao;
import com.example.trubin23.database.asynctasktablenote.AsyncTaskAddNote;
import com.example.trubin23.database.asynctasktablenote.AsyncTaskAddNotes;
import com.example.trubin23.database.asynctasktablenote.AsyncTaskDeleteNote;
import com.example.trubin23.database.asynctasktablenote.AsyncTaskUpdateNote;
import com.example.trubin23.network.RestError;
import com.example.trubin23.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by trubin23 on 21.12.17.
 */

class SyncWithServer {

    enum ResponseState{
        Success,
        UnknownResponseBody,
        ErrorCode,
        NetworkError
    }

    static void notesSync(@NonNull final Context appContext,
                          @NonNull final NoteDao noteDao){
        RetrofitClient.getNotes(new Callback<List<Note>>() {
            final String eventName = appContext.getResources().getString(R.string.notes_sync);

            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                ResponseState responseState = ResponseState.Success;

                if (response.isSuccessful()) {
                    List<Note> notes = response.body();

                    if (notes != null) {
                        LocalBroadcastManager broadcastManager =
                                LocalBroadcastManager.getInstance(appContext);

                        AsyncTaskAddNotes addNotes =
                                new AsyncTaskAddNotes(broadcastManager, noteDao, notes);
                        addNotes.execute();
                    } else {
                        responseState = ResponseState.UnknownResponseBody;
                    }
                } else {
                    RestError restError = RetrofitClient.convertRestError(response.errorBody());
                    responseState = ResponseState.ErrorCode;
                }

                toastShow(appContext, eventName, responseState, response.code());
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                toastShow(appContext, eventName, ResponseState.NetworkError, null);
            }
        });
    }

    static void addNote(@NonNull final Context appContext,
                        @NonNull final NoteDao noteDao, @NonNull final Note note) {
        RetrofitClient.addNote(note, new Callback<Note>() {
            final String eventName = appContext.getResources().getString(R.string.note_added);

            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                ResponseState responseState = ResponseState.Success;

                if (response.isSuccessful()) {
                    Note responseNote = response.body();

                    if (responseNote != null) {
                        LocalBroadcastManager broadcastManager =
                                LocalBroadcastManager.getInstance(appContext);

                        AsyncTaskAddNote addNote =
                                new AsyncTaskAddNote(broadcastManager, noteDao, responseNote);
                        addNote.execute();
                    } else {
                        responseState = ResponseState.UnknownResponseBody;
                    }
                } else {
                    RestError restError = RetrofitClient.convertRestError(response.errorBody());
                    responseState = ResponseState.ErrorCode;
                }

                toastShow(appContext, eventName, responseState, response.code());
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                toastShow(appContext, eventName, ResponseState.NetworkError, null);
            }
        });
    }

    static void updateNote(@NonNull final Context appContext,
                        @NonNull final NoteDao noteDao, @NonNull final Note note) {
        RetrofitClient.updateNote(note, new Callback<Note>() {
            final String eventName = appContext.getResources().getString(R.string.note_updated);

            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                ResponseState responseState = ResponseState.Success;

                if (response.isSuccessful()) {
                    Note responseNote = response.body();

                    if (responseNote != null) {
                        LocalBroadcastManager broadcastManager =
                                LocalBroadcastManager.getInstance(appContext);

                        AsyncTaskUpdateNote updateNote =
                                new AsyncTaskUpdateNote(broadcastManager, noteDao, responseNote);
                        updateNote.execute();
                    } else {
                        responseState = ResponseState.UnknownResponseBody;
                    }
                } else {
                    RestError restError = RetrofitClient.convertRestError(response.errorBody());
                    responseState = ResponseState.ErrorCode;
                }

                toastShow(appContext, eventName, responseState, response.code());
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                toastShow(appContext, eventName, ResponseState.NetworkError, null);
            }
        });
    }

    static void deleteNote(@NonNull final Context appContext,
                           @NonNull final NoteDao noteDao, final @NonNull String id) {
        RetrofitClient.deleteNote(id, new Callback<Note>() {
            final String eventName = appContext.getResources().getString(R.string.note_deleted);

            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                ResponseState responseState = ResponseState.Success;

                if (response.isSuccessful()) {
                    Note responseNote = response.body();

                    if (responseNote != null) {
                        LocalBroadcastManager broadcastManager =
                                LocalBroadcastManager.getInstance(appContext);

                        AsyncTaskDeleteNote deleteNote =
                                new AsyncTaskDeleteNote(broadcastManager, noteDao, id);
                        deleteNote.execute();
                    } else {
                        responseState = ResponseState.UnknownResponseBody;
                    }
                } else {
                    RestError restError = RetrofitClient.convertRestError(response.errorBody());
                    responseState = ResponseState.ErrorCode;
                }

                toastShow(appContext, eventName, responseState, response.code());
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                toastShow(appContext, eventName, ResponseState.NetworkError, null);
            }
        });
    }

    private static void toastShow(@NonNull Context appContext, @NonNull String eventName,
                                  @NonNull ResponseState responseState,
                                  @Nullable Integer responseCode) {
        Resources res = appContext.getResources();

        switch (responseState){
            case Success:
                Toast.makeText(appContext,
                        eventName + "\n"+ res.getString(R.string.success),
                        Toast.LENGTH_SHORT).show();
                break;

            case UnknownResponseBody:
                Toast.makeText(appContext,
                        eventName + "\n"+ res.getString(R.string.unknown_response_body),
                        Toast.LENGTH_SHORT).show();
                break;

            case ErrorCode:
                Toast.makeText(appContext,
                        eventName + "\n" + res.getString(R.string.error_code) + responseCode,
                        Toast.LENGTH_SHORT).show();

                break;
            case NetworkError:

                Toast.makeText(appContext,
                        eventName + "\n" + res.getString(R.string.network_error),
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
