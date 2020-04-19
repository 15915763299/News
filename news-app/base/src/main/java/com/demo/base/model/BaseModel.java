package com.demo.base.model;

import java.lang.ref.WeakReference;

/**
 * M层 -- 基础数据层
 * 分页 - 缓存 - 预制
 * 需要根据父类的规定自定义缓存策略
 */
public abstract class BaseModel<T> extends SuperBaseModel<T> {
    /**
     * 加载网络数据成功
     * 通知所有的注册着加载结果
     */
    protected void loadSuccess(T data) {
        synchronized (this) {
            mUiHandler.postDelayed(() -> {
                for (WeakReference<IBaseModelListener> weakListener : mWeakListenerArrayList) {
                    if (weakListener.get() instanceof BaseModel.ILoadListener) {
                        ILoadListener listenerItem = (ILoadListener) weakListener.get();
                        if (listenerItem != null) {
                            listenerItem.onLoadFinish(BaseModel.this, data);
                        }
                    }
                }
                // 如果我们需要缓存数据，加载成功，让我们保存他到preference
                if (getCachedPreferenceKey() != null) {
                    saveDataToPreference(data);
                }
            }, 0);
        }
    }

    /**
     * 加载网络数据失败
     * 通知所有的注册着加载结果
     */
    protected void loadFail(final String prompt) {
        synchronized (this) {
            mUiHandler.postDelayed(() -> {
                for (WeakReference<IBaseModelListener> weakListener : mWeakListenerArrayList) {
                    if (weakListener.get() instanceof BaseModel.ILoadListener) {
                        ILoadListener listenerItem = (ILoadListener) weakListener.get();
                        if (listenerItem != null) {
                            listenerItem.onLoadFail(BaseModel.this, prompt);
                        }
                    }
                }
            }, 0);
        }
    }

    @Override
    protected void notifyCachedData(T data) {
        loadSuccess(data);
    }

    /**
     * 非分页接口，继承IBaseModelListener
     */
    public interface ILoadListener<T> extends SuperBaseModel.IBaseModelListener {

        void onLoadFinish(BaseModel model, T data);

        void onLoadFail(BaseModel model, String prompt);
    }
}
