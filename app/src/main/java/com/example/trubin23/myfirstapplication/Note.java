package com.example.trubin23.myfirstapplication;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.moshi.Json;

/**
 * Created by trubin23 on 29.11.17.
 */

public class Note implements Parcelable {

    static final String NOTE_UID = "note_uid";

    @Json(name = "uid")
    private String mUid;
    @Json(name = "title")
    private String mTitle;
    @Json(name = "content")
    private String mContent;
    @Json(name = "color")
    private String mColor = "#ffffff";
    @Json(name = "destroy_date")
    private Integer mDestroyDate = Integer.MAX_VALUE;

    public Note(@NonNull String uid, @NonNull String title, @NonNull String content,
                @Nullable String color, @Nullable Integer destroyDate) {
        mUid = uid;
        mTitle = title;
        mContent = content;
        mColor = color;
        mDestroyDate = destroyDate;
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

    @Nullable
    public String getColor() {
        return mColor;
    }

    public void setColor(@Nullable String color) {
        mColor = color;
    }

    @Nullable
    public Integer getDestroyDate() {
        return mDestroyDate;
    }

    public void setDestroyDate(@Nullable Integer destroyDate) {
        mDestroyDate = destroyDate;
    }
}
