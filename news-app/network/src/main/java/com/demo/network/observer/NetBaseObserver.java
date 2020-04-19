package com.demo.network.observer;

import android.util.Log;

import com.demo.base.model.SuperBaseModel;
import com.demo.network.Code;
import com.demo.network.errorhandler.ExceptionHandle;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 观察者基类
 */
public abstract class NetBaseObserver<T> implements Observer<T> {

    private static final String TAG = NetBaseObserver.class.getSimpleName();

    /**
     * M层
     */
    private SuperBaseModel baseModel;

    public NetBaseObserver(SuperBaseModel baseModel) {
        this.baseModel = baseModel;
    }

    @Override
    public void onError(Throwable e) {

        // TODO 统一处理异常

        if (e instanceof ExceptionHandle.ResponseThrowable) {
            ExceptionHandle.ResponseThrowable re = (ExceptionHandle.ResponseThrowable) e;
            Log.e(TAG, "message: " + re.message + ", code: " + re.code);
            onError((ExceptionHandle.ResponseThrowable) e);
        } else {
            Log.e(TAG, e.getMessage());
            onError(new ExceptionHandle.ResponseThrowable(e, Code.Self.UNKNOWN_ERROR));
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (baseModel != null) {
            baseModel.addDisposable(d);
        }
    }


    @Override
    public void onComplete() {
    }


    public abstract void onError(ExceptionHandle.ResponseThrowable e);

}
