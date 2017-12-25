package com.example.trubin23.network;

import android.support.annotation.Nullable;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Andrey on 24.12.2017.
 */

public class ResponseProcessing<T> implements Callback {

    private static final String TAG = "ResponseProcessing";

    @Override
    public void onResponse(Call call, Response response) {
        if (response.isSuccessful()) {
            //noinspection unchecked
            T body = (T) response.body();
            if (body != null) {
                success(body);
            } else {
                successWithoutBody();
            }
        } else {
            RestError restError = RetrofitClient.convertRestError(response.errorBody());
            error(restError);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        Log.e(TAG, "onFailure", t);
    }

    public void success(@Nullable T body) {
    }

    public void successWithoutBody() {
        Log.w(TAG, "success response without body");
    }

    public void error(RestError restError) {
        Log.e(TAG, "RestError code: " + restError.getCode());
    }
}
