package com.example.trubin23.myfirstapplication.domain.notes.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.trubin23.myfirstapplication.presentation.notes.model.Note;

public class NoteDomainModel implements Parcelable {

    private String mUid;
    private String mTitle;
    private String mContent;
    private String mColor;

    public NoteDomainModel(@NonNull String uid, @NonNull String title, @NonNull String content, @NonNull String color) {
        mUid = uid;
        mTitle = title;
        mContent = content;
        mColor = color;
    }

    public NoteDomainModel(@NonNull Parcel in) {
        mUid = in.readString();
        mTitle = in.readString();
        mContent = in.readString();
        mColor = in.readString();
    }

    @NonNull
    public String getUid() {
        return mUid;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    @NonNull
    public String getContent() {
        return mContent;
    }

    @NonNull
    public String getColor() {
        return mColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(mUid);
        parcel.writeString(mTitle);
        parcel.writeString(mContent);
        parcel.writeString(mColor);
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
