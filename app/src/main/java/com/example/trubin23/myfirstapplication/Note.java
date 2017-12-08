package com.example.trubin23.myfirstapplication;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by trubin23 on 29.11.17.
 */

public class Note implements Parcelable {

    static final long DEFAULT_ID = -1;

    private long id;
    private String mTitle;
    private String mText;
    private Date mDate;

    private static final SimpleDateFormat mDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    Note(long id, @NonNull String title, @NonNull String text, Date date) {
        this.id = id;
        this.mTitle = title;
        this.mText = text;
        this.mDate = date;
    }

    static Note createWithoutId(@NonNull String title, @NonNull String text, String dateString) {
        Date date;
        try {
            date = mDateFormat.parse(dateString);
        } catch (ParseException e) {
            date = new Date(0);
        }

        return new Note(DEFAULT_ID, title, text, date);
    }

    static Note createWithoutId(@NonNull String title, @NonNull String text) {
        return new Note(DEFAULT_ID, title, text, new Date());
    }

    private Note(Parcel in) {
        id = in.readLong();
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
        parcel.writeLong(id);
        parcel.writeString(mTitle);
        parcel.writeString(mText);
        parcel.writeSerializable(mDate);
    }

    public long getId() {
        return id;
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
    Date getDate() {
        return mDate;
    }

    @NonNull
    String getDateToString() {
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
