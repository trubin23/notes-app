package com.example.trubin23.myfirstapplication;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.trubin23.database.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by trubin23 on 29.11.17.
 */

public class Note implements Parcelable {

    private long mId;
    private String mTitle;
    private String mText;
    private Date mDate;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public Note(long id, @NonNull String title, @NonNull String text, @Nullable String date) {
        mId = id;
        mTitle = title;
        mText = text;

        try {
            mDate = DATE_FORMAT.parse(date);
        } catch (ParseException | NullPointerException e) {
            mDate =  new Date();
        }
    }

    public Note(@NonNull String title, @NonNull String text, @Nullable String date) {
        this(DatabaseHelper.DEFAULT_ID, title, text, date);
    }

    private Note(@NonNull Parcel parcel) {
        mId = parcel.readLong();
        mTitle = parcel.readString();
        mText = parcel.readString();
        mDate = (Date) parcel.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mText);
        parcel.writeSerializable(mDate);
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
        return DATE_FORMAT.format(mDate);
    }

    void setText(@NonNull String text) {
        mText = text;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(@NonNull Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
