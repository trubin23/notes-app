package com.example.trubin23.myfirstapplication.presentation.common;

import android.util.Log;
import com.example.trubin23.myfirstapplication.domain.common.UseCaseHandler;

/**
 * Created by trubin23 on 27.12.17.
 */

public class BasePresenter<T extends BaseView> implements BasePresenterInterface<T> {

    private static final String TAG = BasePresenter.class.getSimpleName();

    protected UseCaseHandler mUseCaseHandler;

    public BasePresenter(UseCaseHandler useCaseHandler) {
        mUseCaseHandler = useCaseHandler;
    }

    private T mView;

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
