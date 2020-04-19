package com.demo.base.viewmodel;

public interface IMvvmBaseViewModel<V> {

    void attachView(V view);

    V getView();

    boolean isViewAttached();

    void detachView();
}
