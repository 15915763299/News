package com.demo.news.config;

import com.demo.network.INetworkRequestInfo;
import com.demo.news.BuildConfig;

import java.util.HashMap;

/**
 * 统一添加网络请求头部
 */
public class NetworkRequestInfo implements INetworkRequestInfo {
    private HashMap<String, String> headerMap = new HashMap<>(4);

    public NetworkRequestInfo() {
        headerMap.put("os", "android");
        headerMap.put("versionName", BuildConfig.VERSION_NAME);
        headerMap.put("versionCode", String.valueOf(BuildConfig.VERSION_CODE));
        headerMap.put("applicationId", BuildConfig.APPLICATION_ID);
    }

    @Override
    public HashMap<String, String> getRequestHeaderMap() {
        return headerMap;
    }

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
