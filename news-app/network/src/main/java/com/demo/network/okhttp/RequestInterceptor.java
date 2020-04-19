package com.demo.network.okhttp;

import android.text.TextUtils;

import com.demo.base.BaseApp;
import com.demo.base.utils.Md5Utils;
import com.demo.network.INetworkRequestInfo;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 */
class RequestInterceptor implements Interceptor {
    private INetworkRequestInfo mNetworkRequestInfo;

    RequestInterceptor(INetworkRequestInfo networkRequestInfo) {
        this.mNetworkRequestInfo = networkRequestInfo;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        if (mNetworkRequestInfo != null && mNetworkRequestInfo.getRequestHeaderMap() != null) {
            HashMap<String, String> headerMap = mNetworkRequestInfo.getRequestHeaderMap();
            for (String key : mNetworkRequestInfo.getRequestHeaderMap().keySet()) {
                if (!TextUtils.isEmpty(headerMap.get(key))) {
                    builder.addHeader(key, headerMap.get(key));
                }
            }
        }

        if (BaseApp.app.getLoginUser() != null) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String token = BaseApp.app.getLoginUser().token;

            // 简单的验签实现
            builder.addHeader("timestamp", timestamp);
            builder.addHeader("token", token);
            builder.addHeader("sign", Md5Utils.getMd5(timestamp + token));
        }

        return chain.proceed(builder.build());
    }

//    private void log(Request request) {
//        Log.e("Request", request.url().toString());
//        RequestBody requestBody = request.body();
//        if (requestBody != null) {
//            Log.e("Request", requestBody.toString());
//        }
//    }
}