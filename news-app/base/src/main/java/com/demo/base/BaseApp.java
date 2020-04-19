package com.demo.base;

import android.app.Application;
import android.content.Context;

/**
 * @author 尉迟涛
 * create time : 2020/2/28 17:52
 * description :
 */
public class BaseApp extends Application {

    public static BaseApp app;
    public static boolean isDebug;
    public LoginUser loginUser;

    public static void setIsDebug(boolean isDebug) {
        BaseApp.isDebug = isDebug;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        app = this;
    }

    public LoginUser getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(LoginUser loginUser) {
        this.loginUser = loginUser;
    }
}
