package com.demo.network.errorhandler;

import com.demo.network.Code;
import com.demo.network.base.BaseRes;

import io.reactivex.functions.Function;

/**
 * HandleFuc处理以下网络错误：
 * 1、应用数据的错误会抛RuntimeException；
 */
public class AppDataErrorHandler implements Function<BaseRes, BaseRes> {
    @Override
    public BaseRes apply(BaseRes response) {
        // 判断网络返回是否出错，出错就抛出ServerException异常
        if (response != null && (response.code == null || response.code != Code.Server.SUCCESS)) {
            if (response.code == null) {
                response.code = Code.Server.UNKNOWN_ERROR;
            }
            throw new ExceptionHandle.ServerException(response.code, response.msg);
        }
        return response;
    }
}