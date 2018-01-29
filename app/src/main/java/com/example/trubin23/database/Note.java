package com.example.trubin23.database;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.moshi.Json;

/**
 * Created by trubin23 on 29.11.17.
 */

public class Note implements Parcelable {

    public static final String NOTE_UID = "note_uid";

    private static final String DEFAULT_COLOR = "#ffffff";
    private static final Integer DEFAULT_DESTROY_DATE = Integer.MAX_VALUE;

    @Json(name = "uid")
    private String mUid;
    @Json(name = "title")
    private String mTitle;
    @Json(name = "content")
    private String mContent;
    @Json(name = "color")
    private String mColor = DEFAULT_COLOR;
    @Json(name = "destroy_date")
    private Integer mDestroyDate = DEFAULT_DESTROY_DATE;

    public Note(@NonNull String uid, @NonNull String title, @NonNull String content,
                @Nullable String color, @Nullable Integer destroyDate) {
        mUid = uid;
        mTitle = title;
        mContent = content;
        if (color != null) {
            mColor = color;
        }
        if (destroyDate != null) {
            mDestroyDate = destroyDate;
        }
    }

    @SuppressWarnings("unused") // Moshi uses this!
    private Note() {
    }

    public Note(@NonNull Parcel in) {
        mUid = in.readString();
        mTitle = in.readString();
        mContent = in.readString();
        mColor = in.readString();
        mDestroyDate = in.readInt();
    }

    @NonNull
    public String getUid() {
        return mUid;
    }

    public void setUid(@NonNull String uid) {
        mUid = uid;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(@NonNull String title) {
        mTitle = title;
    }

    @NonNull
    public String getContent() {
        return mContent;
    }

    public void setContent(@NonNull String content) {
        mContent = content;
    }

    @NonNull
    public String getColor() {
        return mColor;
    }

    public void setColor(@Nullable String color) {
        if (color != null) {
            mColor = color;
        } else {
            mColor = DEFAULT_COLOR;
        }
    }

    @NonNull
    Integer getDestroyDate() {
        return mDestroyDate;
    }

    public void setDestroyDate(@Nullable Integer destroyDate) {
        if (destroyDate != null) {
            mDestroyDate = destroyDate;
        } else {
            mDestroyDate = DEFAULT_DESTROY_DATE;
        }
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
        parcel.writeInt(mDestroyDate);
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
