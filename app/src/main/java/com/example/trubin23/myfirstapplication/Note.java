package com.example.trubin23.myfirstapplication;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.trubin23.database.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by trubin23 on 29.11.17.
 */

public class Note {

    private long mId;
    private String mTitle;
    private String mText;
    private Date mDate;

    private static final SimpleDateFormat mDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public Note(long id, @NonNull String title, @NonNull String text, @Nullable String date) {
        this.mId = id;
        this.mTitle = title;
        this.mText = text;

        try {
            this.mDate = mDateFormat.parse(date);
        } catch (ParseException | NullPointerException e) {
            this.mDate =  new Date();
        }
    }

    public Note(@NonNull String title, @NonNull String text, @Nullable String date) {
        this(DatabaseHelper.DEFAULT_ID, title, text, date);
    }

    public long getId() {
        return mId;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    @NonNull
    public String getText() {
        return mText;
    }

    @NonNull
    public String getDate() {
        return mDateFormat.format(mDate);
    }

    void setText(@NonNull String text) {
        this.mText = text;
    }
}
