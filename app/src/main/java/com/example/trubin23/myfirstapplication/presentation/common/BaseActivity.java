package com.example.trubin23.myfirstapplication.presentation.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by trubin23 on 27.12.17.
 */

public class BaseActivity extends AppCompatActivity implements BaseView {

    @Nullable
    private BasePresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null){
            mPresenter.bind(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null){
            mPresenter.unbind();
        }
    }

    protected void bindPresenterToView(BasePresenter presenter){
        mPresenter = presenter;
    }
}
