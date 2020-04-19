package com.demo.c.user.processer;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCUtil;
import com.demo.c.user.login.ActLogin;

/**
 * 登录
 * @author billy.qi
 */
public class PLogin implements IActionProcessor {

    public static final String PROCESSOR_NAME = "Login";

    @Override
    public String getActionName() {
        return PROCESSOR_NAME;
    }

    @Override
    public boolean onActionCall(CC cc) {
        CCUtil.navigateTo(cc, ActLogin.class);
        //不立即调用CC.sendCCResult,返回true
        return true;
    }
}
