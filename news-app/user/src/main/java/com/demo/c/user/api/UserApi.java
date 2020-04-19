package com.demo.c.user.api;

import com.demo.base.LoginUser;
import com.demo.network.Constants;
import com.demo.network.base.BaseApi;
import com.demo.network.base.BaseRes;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;

/**
 * @author 尉迟涛
 * create time : 2020/3/17 0:44
 * description :
 */
public class UserApi extends BaseApi {
    private static class Instance {
        private static UserApi instance = new UserApi();
    }

    public static UserApi get() {
        return Instance.instance;
    }

    private UserApiInterface userApiInterface;

    private UserApi() {
        super(Constants.CLOUD_URL);
        userApiInterface = retrofit.create(UserApiInterface.class);
    }

    public void validateCode(Observer<BaseRes<String>> observer, String phone) {
        Map<String, String> params = new HashMap<>(1);
        params.put("phone", phone);
        ApiSubscribe(userApiInterface.register(params), observer);
    }

    public void register(Observer<BaseRes<String>> observer, String phone, String password, String code) {
        Map<String, String> params = new HashMap<>(2);
        params.put("phone", phone);
        params.put("password", password);
        params.put("code", code);
        ApiSubscribe(userApiInterface.register(params), observer);
    }

    public void login(Observer<BaseRes<LoginUser>> observer, String phone, String password) {
        Map<String, String> params = new HashMap<>(2);
        params.put("phone", phone);
        params.put("password", password);
        ApiSubscribe(userApiInterface.login(params), observer);
    }
}
