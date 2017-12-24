package com.example.trubin23.network;

import com.example.trubin23.myfirstapplication.Note;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by trubin23 on 19.12.17.
 */
public class RetrofitClientTest {

    private static final String mTempUid = "d0a9d8f8-0010-4b94-8699-1efd9ae84eeb";

    @Test
    public void getNotes() {
        final CountDownLatch signal = new CountDownLatch(1);

        RetrofitClient.getNotes(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                if (response.isSuccessful()) {
                    List<Note> notes = response.body();
                    signal.countDown();
                } else {
                    RestError restError = RetrofitClient.convertRestError(response.errorBody());
                    signal.countDown();
                }
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                signal.countDown();
            }
        });

        try {
            signal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getNote() {
        final CountDownLatch signal = new CountDownLatch(1);

        RetrofitClient.getNote(mTempUid, new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if (response.isSuccessful()) {
                    Note note = response.body();
                    signal.countDown();
                } else {
                    RestError restError = RetrofitClient.convertRestError(response.errorBody());
                    signal.countDown();
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                signal.countDown();
            }
        });

        try {
            signal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addNote() {
        final CountDownLatch signal = new CountDownLatch(1);

        Note note = new Note(mTempUid, "title 0006",
                "content 0006", null, null);
        //note.setColor("#000200");
        //note.setDestroyDate(10002);

        RetrofitClient.addNote(note, new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if (response.isSuccessful()) {
                    Note responseNote = response.body();
                    signal.countDown();
                } else {
                    RestError restError = RetrofitClient.convertRestError(response.errorBody());
                    signal.countDown();
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                signal.countDown();
            }
        });

        try {
            signal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateNote() {
        final CountDownLatch signal = new CountDownLatch(1);

        Note note = new Note(mTempUid, "title 0104", "content 0104",
                "#bbbbff", 10104);

        RetrofitClient.updateNote(note, new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if (response.isSuccessful()) {
                    Note responseNote = response.body();
                    signal.countDown();
                } else {
                    RestError restError = RetrofitClient.convertRestError(response.errorBody());
                    signal.countDown();
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                signal.countDown();
            }
        });

        try {
            signal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteNote() {
        final CountDownLatch signal = new CountDownLatch(1);

        RetrofitClient.deleteNote(mTempUid, new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if (response.isSuccessful()) {
                    Note responseNote = response.body();
                    signal.countDown();
                } else {
                    RestError restError = RetrofitClient.convertRestError(response.errorBody());
                    signal.countDown();
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                signal.countDown();
            }
        });

        try {
            signal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}