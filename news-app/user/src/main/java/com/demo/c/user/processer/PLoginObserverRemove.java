package com.demo.c.user.processer;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.demo.c.user.UserStateManager;

/**
 * 删除监听登录状态的动态组件
 * @author billy.qi
 */
public class PLoginObserverRemove implements IActionProcessor {

    public static final String PROCESSOR_NAME = "RemoveLoginObserver";

    @Override
    public String getActionName() {
        return PROCESSOR_NAME;
    }

    @Override
    public boolean onActionCall(CC cc) {
        String dynamicComponentName = cc.getParamItem("componentName");
        UserStateManager.removeObserver(dynamicComponentName);
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
