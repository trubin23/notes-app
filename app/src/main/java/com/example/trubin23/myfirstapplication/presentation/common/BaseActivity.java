package com.example.trubin23.myfirstapplication.presentation.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.example.trubin23.myfirstapplication.domain.common.UseCaseHandler;

/**
 * Created by trubin23 on 27.12.17.
 */

public class BaseActivity extends AppCompatActivity implements BaseView {

    protected UseCaseHandler mUseCaseHandler;

    @Nullable
    private BasePresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
    }

    private void injectDependencies() {
        mUseCaseHandler = UseCaseHandler.getInstance();
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
