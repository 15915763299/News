package com.demo.c.news;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.demo.c.news.headlinenews.FragHeadlineNews;

/**
 * https://qibilly.com/CC-website/#/manual-IComponent
 */
public class NewsComponent implements IComponent {

    public static final String FRAG_HEADLINE_NEWS = "HeadlineNews";

    @Override
    public String getName() {
        return "News";
    }

    /**
     * 调用此组件时执行的方法（此方法只在LocalCCInterceptor中被调用）
     * 注：执行完成后必须调用CC.sendCCResult(callId, CCResult.success(result));
     * cc.getContext() android的context，在组件被跨进程调用时，返回application对象
     * cc.getAction() 调用的action
     * cc.getParams() 调用参数
     * cc.getCallId() 调用id，用于通过CC向调用方发送调用结果
     * @param cc 调用信息
     * @return 是否延迟回调结果 {@link CC#sendCCResult(String callId, CCResult result)}
     *          false:否(同步实现，在return之前回调结果)
     *          true:是(异步实现，本次CC调用将等待回调结果)
     */
    @Override
    public boolean onCall(CC cc) {
        // 根据action采取不同的策略
        String actionName = cc.getActionName();
        switch (actionName) {
            case FRAG_HEADLINE_NEWS:
                CCResult result = new CCResult();
                result.addData(FRAG_HEADLINE_NEWS, new FragHeadlineNews());
                //每次onCall被调用后，必须调用CC.sendCCResult(callId, result)将调用结果发送给调用方
                CC.sendCCResult(cc.getCallId(), result);
                return false;
            default:
                //其它actionName当前组件暂时不能响应，可以通过如下方式返回状态码为-12的CCResult给调用方
                CC.sendCCResult(cc.getCallId(), CCResult.errorUnsupportedActionName());
                return false;
        }
    }
}
