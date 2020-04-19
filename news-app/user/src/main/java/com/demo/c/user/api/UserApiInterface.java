package com.demo.c.user.api;

import com.demo.base.LoginUser;
import com.demo.network.base.BaseRes;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * @author 尉迟涛
 * create time : 2020/3/17 0:42
 * description :
 */
public interface UserApiInterface {

    @POST("user/validateCode")
    Observable<BaseRes<String>> validateCode(@QueryMap Map<String, String> options);

    @POST("user/register")
    Observable<BaseRes> register(@Body Map<String, String> options);

    @POST("user/login")
    Observable<BaseRes<LoginUser>> login(@Body Map<String, String> options);

}
