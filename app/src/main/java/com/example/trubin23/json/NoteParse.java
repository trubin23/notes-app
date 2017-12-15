package com.example.trubin23.json;

import android.support.annotation.NonNull;

/**
 * Created by trubin23 on 05.12.17.
 */

public final class NoteParse {
    final String mTitle;
    final String mText;
    final String mDate;


    public NoteParse(@NonNull String title, @NonNull String text, @NonNull String date) {
        mTitle = title;
        mText = text;
        mDate = date;
    }
}
