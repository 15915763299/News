package com.demo.network.okhttp;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 *
 */
public class ResponseInterceptor implements Interceptor {

    ResponseInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            throw e;
        }

        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            responseBody = ResponseBody.create(null, "");
        } else {
            responseBody = ResponseBody.create(responseBody.contentType(), responseBody.string());
        }
        return response.newBuilder().body(responseBody).build();
    }

//    private void log(ResponseBody responseBody) throws IOException {
//        if (responseBody == null) {
//            Log.e("Response", "null");
//        } else {
//            Log.e("Response", responseBody.toString());
//        }
//    }
}
