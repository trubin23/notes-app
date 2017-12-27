package com.example.trubin23.myfirstapplication.domain.common;


/**
 * Base use case to be extended for implementation of business logic to be run with the {@link UseCaseHandler} class
 *
 * @param <Q> extends {@link BaseUseCase.RequestValues} and indicate the data passed to a request
 * @param <P> extends {@link BaseUseCase.ResponseValues} and indicate the data received from a request
 */
public abstract class BaseUseCase<Q extends BaseUseCase.RequestValues, P extends BaseUseCase.ResponseValues> {

    private Q mRequestValues;
    private UseCaseCallback<P> mUseCaseCallback;

    void setRequestValues(Q requestValues) {
        mRequestValues = requestValues;
    }

    public Q getRequestValues() {
        return mRequestValues;
    }

    protected UseCaseCallback<P> getUseCaseCallback() {
        return mUseCaseCallback;
    }

    public void setUseCaseCallback(UseCaseCallback<P> useCaseCallback) {
        mUseCaseCallback = useCaseCallback;
    }

    void run() {
        executeUseCase(mRequestValues);
    }

    protected abstract void executeUseCase(Q requestValues);

    /**
     * Data passed to a request.
     */
    public interface RequestValues {
    }

    /**
     * Data received from a request.
     */
    public interface ResponseValues {
    }

    public interface UseCaseCallback<R> {
        void onSuccess(R response);
        void onError();
    }
}