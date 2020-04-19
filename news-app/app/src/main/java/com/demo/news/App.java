package com.demo.news;

import android.content.Context;

import com.billy.cc.core.component.CC;
import com.demo.base.BaseApp;
import com.demo.base.loadsir.CustomCallback;
import com.demo.base.loadsir.ErrorCallback;
import com.demo.base.loadsir.LottieEmptyCallback;
import com.demo.base.loadsir.LottieLoadingCallback;
import com.demo.base.loadsir.TimeoutCallback;
import com.demo.network.okhttp.OkHttpInstance;
import com.demo.news.config.NetworkRequestInfo;
import com.kingja.loadsir.core.LoadSir;

/**
 * @author 尉迟涛
 * create time : 2020/2/28 17:55
 * description :
 */
public class App extends BaseApp {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        OkHttpInstance.setNetworkRequestInfo(new NetworkRequestInfo());
        setIsDebug(BuildConfig.DEBUG);

        // 设置LoadSir的各类状态View
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new LottieEmptyCallback())
                .addCallback(new LottieLoadingCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())
                .setDefaultCallback(LottieLoadingCallback.class)
                .commit();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Stetho調試，訪問：chrome://inspect/
        //Stetho.initializeWithDefaults(this);

        // 开启调试模式
        CC.enableDebug(BuildConfig.DEBUG);
        // 开启日志
        CC.enableVerboseLog(BuildConfig.DEBUG);
        // 开启跨组件调用
        CC.enableRemoteCC(BuildConfig.DEBUG);
    }
}
