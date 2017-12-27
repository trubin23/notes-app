package com.example.trubin23.myfirstapplication.storage.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.moshi.Json;

/**
 * Created by trubin23 on 19.12.17.
 */

public class RestError {

    @Json(name = "code")
    private Integer code;
    @Json(name = "message")
    private String message;
    @Json(name = "metadata")
    private Object metadata;
    @Json(name = "error")
    private Boolean error;

    @NonNull
    public Integer getCode() {
        return code;
    }

    public void setCode(@NonNull Integer code) {
        this.code = code;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    public void setMessage(@Nullable String message) {
        this.message = message;
    }

    @Nullable
    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(@Nullable Object metadata) {
        this.metadata = metadata;
    }

    @Nullable
    public Boolean getError() {
        return error;
    }

    public void setError(@Nullable Boolean error) {
        this.error = error;
    }

}
