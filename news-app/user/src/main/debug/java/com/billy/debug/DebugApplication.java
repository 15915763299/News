package com.billy.debug;

import com.billy.cc.core.component.CC;
import com.demo.base.BaseApp;
import com.demo.base.loadsir.CustomCallback;
import com.demo.base.loadsir.ErrorCallback;
import com.demo.base.loadsir.LottieEmptyCallback;
import com.demo.base.loadsir.LottieLoadingCallback;
import com.demo.base.loadsir.TimeoutCallback;
import com.demo.c.user.BuildConfig;
import com.demo.network.INetworkRequestInfo;
import com.demo.network.okhttp.OkHttpInstance;
import com.kingja.loadsir.core.LoadSir;

import java.util.HashMap;

/**
 * @author 尉迟涛
 * create time : 2020/3/11 16:58
 * description :
 */
public class DebugApplication extends BaseApp {
    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpInstance.setNetworkRequestInfo(new INetworkRequestInfo() {
            @Override
            public HashMap<String, String> getRequestHeaderMap() {
                return null;
            }

            @Override
            public boolean isDebug() {
                return BuildConfig.DEBUG;
            }
        });

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

        CC.enableDebug(BuildConfig.DEBUG);
        CC.enableVerboseLog(BuildConfig.DEBUG);
        CC.enableRemoteCC(BuildConfig.DEBUG);
    }
}
