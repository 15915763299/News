package com.demo.c.user.processer;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.demo.c.user.usercenter.FragUserCenter;

/**
 * @author 尉迟涛
 * create time : 2020/4/19 17:38
 * description :
 */
public class PUserCenter implements IActionProcessor{

    public static final String PROCESSOR_NAME = "UserCenter";

    @Override
    public String getActionName() {
        return PROCESSOR_NAME;
    }

    @Override
    public boolean onActionCall(CC cc) {
        CCResult result = new CCResult();
        result.addData(PROCESSOR_NAME, new FragUserCenter());
        //每次onCall被调用后，必须调用CC.sendCCResult(callId, result)将调用结果发送给调用方
        CC.sendCCResult(cc.getCallId(), result);
        return true;
    }
}
