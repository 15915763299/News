package com.demo.network.base;

import com.demo.network.errorhandler.AppDataErrorHandler;
import com.demo.network.errorhandler.HttpErrorHandler;
import com.demo.network.okhttp.OkHttpInstance;

import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 封装RxJava的使用
 */
public abstract class BaseApi {

    private static ErrorTransformer sErrorTransformer = new ErrorTransformer();
    /**
     * Retrofit缓存，初始长度为可能的baseUrl种类数
     */
    private static ConcurrentHashMap<String, Retrofit> retrofitMap = new ConcurrentHashMap<>(1);

    protected Retrofit retrofit;

    protected BaseApi(String baseUrl) {
        retrofit = retrofitMap.get(baseUrl);
        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .client(OkHttpInstance.get())
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create()).build();
            retrofitMap.put(baseUrl, retrofit);
        }
    }

    /**
     * 封装线程管理和订阅的过程
     */
    protected void ApiSubscribe(Observable observable, Observer observer) {
        observable = observable
                // 指定事件源代码执行的线程（Observable、doOnCompleted()、doOnError()）
                .subscribeOn(Schedulers.io())
                // 指定doOnUnsubscribe()的线程。https://www.jianshu.com/p/ce5254b8c12d
                .unsubscribeOn(Schedulers.io())
                // 指定订阅者代码执行的线程
                .observeOn(AndroidSchedulers.mainThread());

        observable.compose(sErrorTransformer)
                //参数是我们创建的一个订阅者，在这里与事件流建立订阅关系
                .subscribe(observer);
    }

    /**
     * 处理错误的变换
     * 网络请求的错误处理，其中网络错误分为两类：
     * 1、http请求相关的错误，例如：404，403，socket timeout等等；
     * 2、http请求正常，但是返回的应用数据里提示发生了异常，表明服务器已经接收到了来自客户端的请求，但是由于
     * 某些原因，服务器没有正常处理完请求，可能是缺少参数，或者其他原因；
     * <p>
     * Transformers: 提供给他一个Observable它会返回给你另一个Observable，这和内联一系列操作符有着同等功效。
     */
    private static class ErrorTransformer<T> implements ObservableTransformer {

        @Override
        public ObservableSource apply(Observable upstream) {
            // onErrorResumeNext：当抛出异常的时候不去调用 onError，而是交给另外一个Observable来解决这个异常
            return (Observable<T>) upstream
                    // 返回的数据统一错误处理
                    .map(new AppDataErrorHandler())
                    // Http 错误处理
                    .onErrorResumeNext(new HttpErrorHandler<T>());
        }
    }
}
