package com.demo.c.user;

import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.demo.base.LoginUser;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户登录状态管理，该类中全为静态方法、静态变量
 * 功能：
 * 1、静态变量记录用户状态
 * 2、可以注册组件，观察用户状态变化
 * <p>
 * 原作者：billy.qi
 */
public class UserStateManager {
    public static final String KEY_USER = "user";

    /**
     * 当前登录用户
     */
    private static LoginUser loginUser;

    /**
     * 存储当前监听登录状态的所有组件的 名称-action 键值对
     */
    private static final Map<String, String> USER_LOGIN_OBSERVER = new ConcurrentHashMap<>();

    /**
     * 添加观察组件
     */
    public static boolean addObserver(String componentName, String actionName) {
        if (!TextUtils.isEmpty(componentName)) {
            USER_LOGIN_OBSERVER.put(componentName, actionName);
            //开始监听时，立即返回当前的登录状态
            onUserLoginStateUpdated(componentName, actionName);
            return true;
        }
        return false;
    }

    /**
     * 移除观察组件
     */
    public static void removeObserver(String componentName) {
        USER_LOGIN_OBSERVER.remove(componentName);
    }

    private static void onUserLoginStateUpdated() {
        //登录状态改变时，立即通知所有监听登录状态的组件
        if (!USER_LOGIN_OBSERVER.isEmpty()) {
            Set<Map.Entry<String, String>> entries = USER_LOGIN_OBSERVER.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                onUserLoginStateUpdated(entry.getKey(), entry.getValue());
            }
        }
    }

    private static void onUserLoginStateUpdated(String componentName, String actionName) {
        CC.obtainBuilder(componentName)
                .setActionName(actionName)
                .addParam(KEY_USER, loginUser)
                .build().callAsync();
    }

    public static void setLoginUser(LoginUser user) {
        if (user != loginUser) {
            loginUser = user;
            onUserLoginStateUpdated();
        }
    }

    public static LoginUser getLoginUser() {
        return loginUser;
    }

}
