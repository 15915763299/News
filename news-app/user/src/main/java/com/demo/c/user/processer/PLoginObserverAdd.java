package com.demo.c.user.processer;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.demo.c.user.UserStateManager;

/**
 * 添加监听登录状态的动态组件
 * @author billy.qi
 */
public class PLoginObserverAdd implements IActionProcessor {

    public static final String PROCESSOR_NAME = "AddLoginObserver";

    @Override
    public String getActionName() {
        return PROCESSOR_NAME;
    }

    @Override
    public boolean onActionCall(CC cc) {
        boolean success = UserStateManager.addObserver(
                cc.getParamItem("componentName"),
                cc.getParamItem("actionName")
        );
        CCResult result = success ? CCResult.success() : CCResult.error("");
        CC.sendCCResult(cc.getCallId(), result);
        return false;
    }
}
