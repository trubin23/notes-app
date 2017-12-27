package com.example.trubin23.myfirstapplication.domain.common;


interface UseCaseScheduler {

    void execute(Runnable runnable);

    <V extends BaseUseCase.ResponseValues> void onSuccess(final V response,
                                                          final BaseUseCase.UseCaseCallback<V> useCaseCallback);

    <V extends BaseUseCase.ResponseValues> void onError(
            final BaseUseCase.UseCaseCallback<V> useCaseCallback);
}