package com.demo.base.model;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.CallSuper;

import com.demo.base.utils.GsonUtils;
import com.demo.base.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 基础数据层（父类）
 * 功能：
 * 1、注册、取消监听 IBaseModelListener
 * 2、主动取消订阅
 * 3、磁盘缓存网络数据
 */
public abstract class SuperBaseModel<T> {
    /**
     * 回调主线程
     */
    protected Handler mUiHandler = new Handler(Looper.getMainLooper());
    /**
     * 被回收对象的队列
     * ============================================
     * 检测一个对象是否被回收，使用 Reference + ReferenceQueue，大概需要几个步骤：
     * 创建一个引用队列 queue
     * 创建 Reference 对象，并关联引用队列 queue
     * 在 reference 被回收的时候，Reference 会被添加到 queue 中
     * ============================================
     * 参考：https://blog.csdn.net/gdutxiaoxu/article/details/80738581
     */
    protected ReferenceQueue<IBaseModelListener> mReferenceQueue;
    /**
     * 阻塞队列
     */
    protected ConcurrentLinkedQueue<WeakReference<IBaseModelListener>> mWeakListenerArrayList;
    /**
     * 缓存的最小单位，给需要缓存的数据加上一个时间戳
     */
    private BaseCachedData<T> mData;

    public SuperBaseModel() {
        mReferenceQueue = new ReferenceQueue<>();
        mWeakListenerArrayList = new ConcurrentLinkedQueue<>();
        if (getCachedPreferenceKey() != null) {
            mData = new BaseCachedData<>();
        }
    }

    //***************************************************************************
    // 管理监听器
    //***************************************************************************

    protected interface IBaseModelListener {
    }

    /**
     * 注册监听
     */
    public void register(IBaseModelListener listener) {
        if (listener == null) {
            return;
        }

        synchronized (this) {
            // 每次注册的时候清理已经被系统回收的对象，mReferenceQueue中的对象即为已被清理的对象
            Reference<? extends IBaseModelListener> releaseListener;
            while ((releaseListener = mReferenceQueue.poll()) != null) {
                mWeakListenerArrayList.remove(releaseListener);
            }

            for (WeakReference<IBaseModelListener> weakListener : mWeakListenerArrayList) {
                IBaseModelListener listenerItem = weakListener.get();
                if (listenerItem == listener) {
                    return;
                }
            }
            // 创建 Reference 对象，并关联引用队列 queue
            WeakReference<IBaseModelListener> weakListener = new WeakReference<>(listener, mReferenceQueue);
            mWeakListenerArrayList.add(weakListener);
        }
    }

    /**
     * 取消监听
     */
    public void unRegister(IBaseModelListener listener) {
        if (listener == null) {
            return;
        }

        synchronized (this) {
            for (WeakReference<IBaseModelListener> weakListener : mWeakListenerArrayList) {
                IBaseModelListener listenerItem = weakListener.get();
                if (listener == listenerItem) {
                    mWeakListenerArrayList.remove(weakListener);
                    break;
                }
            }
        }
    }

    //***************************************************************************
    // CompositeDisposable
    //***************************************************************************

    /**
     * RxJava容易造成内存泄漏，在某些情况下没有及时取消订阅导致内存泄漏。
     * CompositeDisposable可以将Disposable统一管理。
     */
    private CompositeDisposable compositeDisposable;

    public void addDisposable(Disposable d) {
        if (d == null) {
            return;
        }
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(d);
    }

    @CallSuper
    public void cancel() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            // 主动解除订阅
            compositeDisposable.dispose();
        }
    }

    //***************************************************************************
    // 抽象方法与需要重写的方法
    //***************************************************************************

    public abstract void refresh();

    protected abstract void load();

    protected abstract void notifyCachedData(T data);

    /**
     * 该model的数据是否需要缓存，如果需要请返回缓存的key
     */
    protected String getCachedPreferenceKey() {
        return null;
    }

    /**
     * 缓存的数据的类型
     */
    protected Type getTClass() {
        return null;
    }

    /**
     * 该model的数据是否有apk预制的数据，如果有请返回，默认没有
     */
    protected String getApkString() {
        return null;
    }

    /**
     * 是否更新数据，可以在这里设计策略，可以是一天一次，一月一次等等，
     * 默认是每次请求都更新
     * 该方法在缓存存在的时候会被调用
     *
     * @param updateTimeInMills 上次更新的时间
     * @return 是否需要请求网络，进行数据更新
     */
    protected boolean isNeedToUpdate(long updateTimeInMills) {
        return true;
    }


    //***************************************************************************
    // 缓存网络数据，不同页面定制不同缓存策略
    //***************************************************************************

    /**
     * 保存网络数据至磁盘
     * 对于App首页：
     * 为了保证App打开的时候由于网络慢或者异常的情况下TabLayout不为空，所以App对渠道数据进行了预制；
     * 网络请求后会缓存数据至磁盘，今后打开App都会从SharedPreference中读取，而不在读取Apk预制数据
     * {@link SuperBaseModel#getApkString}。由于渠道数据变化没那么快，我们可以设置成一天只加载一次。
     */
    protected void saveDataToPreference(T data) {
        mData.data = data;
        mData.updateTimeInMills = System.currentTimeMillis();
        SpUtils.getNetSp().put(getCachedPreferenceKey(), GsonUtils.toJson(mData));
    }

    public void getCachedDataAndLoad() {
        if (getCachedPreferenceKey() != null) {
            // key不为空则去读缓存
            String saveDataString = SpUtils.getNetSp().getString(getCachedPreferenceKey());
            if (!TextUtils.isEmpty(saveDataString)) {
                // 缓存不为空则反序列化
                try {
                    JSONObject jsonObject = new JSONObject(saveDataString);
                    T savedData = GsonUtils.fromLocalJson(
                            jsonObject.getString(BaseCachedData.DATA_NAME),
                            getTClass()
                    );

                    if (savedData != null) {
                        // 查看是否需要更新数据，isNeedToUpdate可以定制更新策略
                        long time = jsonObject.getLong(BaseCachedData.TIME_NAME);
                        if (isNeedToUpdate(time)) {
                            load();
                        } else {
                            notifyCachedData(savedData);
                        }
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // 如果没有缓存，查看有无Apk缓存
            if (getApkString() != null) {
                notifyCachedData(GsonUtils.fromLocalJson(getApkString(), getTClass()));
            }
        }
        load();
    }
}
