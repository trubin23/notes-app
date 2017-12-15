package com.example.trubin23.json;

import com.squareup.moshi.Json;

/**
 * Created by trubin23 on 05.12.17.
 */

public final class NoteParse {
    @Json(name = "title") final String mTitle;
    @Json(name = "text") final String mText;
    @Json(name = "date") final String mDate;

    public NoteParse(String title, String text, String date) {
        mTitle = title;
        mText = text;
        mDate = date;
    }
}
