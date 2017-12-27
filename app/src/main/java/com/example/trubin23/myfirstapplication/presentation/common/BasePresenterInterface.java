package com.example.trubin23.myfirstapplication.presentation.common;

/**
 * Created by trubin23 on 27.12.17.
 */

interface BasePresenterInterface<T> {

    void bind(T view);

    void unbind();

    T getView();
}