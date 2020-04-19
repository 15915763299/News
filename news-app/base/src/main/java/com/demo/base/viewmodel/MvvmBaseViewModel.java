package com.demo.base.viewmodel;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.demo.base.model.SuperBaseModel;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * VM层 -- 中间层
 */
public class MvvmBaseViewModel<V, M extends SuperBaseModel>
        extends ViewModel implements IMvvmBaseViewModel<V> {
    /**
     * V层引用
     */
    private Reference<V> mUIRef;
    /**
     * M层（多个）
     */
    protected M model;

    /**
     * 关联V层
     *
     * @param view Fragment或Activity
     */
    public void attachView(V view) {
        mUIRef = new WeakReference<>(view);
    }

    @Nullable
    public V getView() {
        if (mUIRef == null) {
            return null;
        }
        return mUIRef.get();
    }

    public boolean isViewAttached() {
        return mUIRef != null && mUIRef.get() != null;
    }

    public void detachView() {
        if (mUIRef != null) {
            mUIRef.clear();
            mUIRef = null;
        }
        if (model != null) {
            model.cancel();
        }
    }
}
