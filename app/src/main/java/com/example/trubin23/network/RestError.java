package com.example.trubin23.network;

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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

}
