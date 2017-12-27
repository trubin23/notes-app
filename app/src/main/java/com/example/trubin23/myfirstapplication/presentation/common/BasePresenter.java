package com.example.trubin23.myfirstapplication.presentation.common;

import android.util.Log;

/**
 * Created by trubin23 on 27.12.17.
 */

public class BasePresenter<T extends BaseView> implements BasePresenterInterface<T> {

    private static final String TAG = BasePresenter.class.getSimpleName();

    private T mView;

    public BasePresenter() {
    }

    @Override
    public void bind(T view) {
        mView = view;
        Log.d(TAG, "binding presenter [" + this + "] with view [" + mView + "]");
    }

    @Override
    public void unbind() {
        Log.d(TAG, "unbinding presenter [" + this + "] with view [" + mView + "]");
        mView = null;
    }

    @Override
    public T getView() {
        return mView;
    }
}
