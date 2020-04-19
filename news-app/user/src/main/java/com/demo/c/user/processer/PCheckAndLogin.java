package com.demo.c.user.processer;

import android.os.Handler;
import android.os.Looper;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.CCUtil;
import com.demo.base.utils.ToastUtils;
import com.demo.c.user.UserStateManager;
import com.demo.c.user.login.ActLogin;


/**
 * Check user login state:<br>
 * return success if user is already login<br>
 * otherwise start the LoginActivity and return loginResult after login is finished
 *
 * @author billy.qi
 */
public class PCheckAndLogin implements IActionProcessor {

    public static final String PROCESSOR_NAME = "CheckAndLogin";

    @Override
    public String getActionName() {
        return PROCESSOR_NAME;
    }

    @Override
    public boolean onActionCall(CC cc) {
        if (UserStateManager.getLoginUser() != null) {
            //already login, return username
            CCResult result = CCResult.success(UserStateManager.KEY_USER, UserStateManager.getLoginUser());
            CC.sendCCResult(cc.getCallId(), result);
            return false;
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> ToastUtils.showLong("请先登录"));
        CCUtil.navigateTo(cc, ActLogin.class);
        //不立即调用CC.sendCCResult,返回true
        return true;
    }
}
