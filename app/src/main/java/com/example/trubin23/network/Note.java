package com.example.trubin23.network;

import com.squareup.moshi.Json;

/**
 * Created by trubin23 on 18.12.17.
 */

public class Note {

    @Json(name = "uid")
    private String uid;
    @Json(name = "title")
    private String title;
    @Json(name = "color")
    private String color;
    @Json(name = "destroy_date")
    private Integer destroyDate;
    @Json(name = "content")
    private String content;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getDestroyDate() {
        return destroyDate;
    }

    public void setDestroyDate(Integer destroyDate) {
        this.destroyDate = destroyDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}