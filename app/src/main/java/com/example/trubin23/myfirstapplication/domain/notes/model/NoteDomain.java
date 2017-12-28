package com.example.trubin23.myfirstapplication.domain.notes.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.trubin23.myfirstapplication.presentation.notes.model.NoteView;

public class NoteDomain implements Parcelable {

    private String mUid;
    private String mTitle;
    private String mContent;
    private String mColor;

    public NoteDomain(@NonNull String uid, @NonNull String title, @NonNull String content, @NonNull String color) {
        mUid = uid;
        mTitle = title;
        mContent = content;
        mColor = color;
    }

    public NoteDomain(@NonNull Parcel in) {
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

    public static final Creator<NoteView> CREATOR = new Creator<NoteView>() {
        @Override
        public NoteView createFromParcel(@NonNull Parcel in) {
            return new NoteView(in);
        }

        @Override
        public NoteView[] newArray(int size) {
            return new NoteView[size];
        }
    };
}