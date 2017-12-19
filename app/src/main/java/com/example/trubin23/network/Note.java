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
    @Json(name = "content")
    private String content;
    @Json(name = "color")
    private String color = "#ffffff";
    @Json(name = "destroy_date")
    private Integer destroyDate = Integer.MAX_VALUE;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

}