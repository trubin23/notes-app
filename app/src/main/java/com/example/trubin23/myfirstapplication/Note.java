package com.example.trubin23.myfirstapplication;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by trubin23 on 29.11.17.
 */

public class Note implements Parcelable {

    static final long DEFAULT_ID = -1;

    private long mId;
    private String mTitle;
    private String mText;
    private Date mDate;

    private static final SimpleDateFormat mDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    Note(long id, @NonNull String title, @NonNull String text, @Nullable String date) {
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
        this(DEFAULT_ID, title, text, date);
    }

    Note(@NonNull String title, @NonNull String text) {
        this(DEFAULT_ID, title, text, null);
    }

    private Note(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
        mText = in.readString();
        mDate = (Date) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mText);
        parcel.writeSerializable(mDate);
    }

    public long getId() {
        return mId;
    }

    @NonNull
    String getTitle() {
        return mTitle;
    }

    @NonNull
    String getText() {
        return mText;
    }

    @NonNull
    String getDate() {
        return mDateFormat.format(mDate);
    }

    void setText(@NonNull String text) {
        this.mText = text;
    }

    void setDate(@NonNull Date date) {
        this.mDate = date;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
