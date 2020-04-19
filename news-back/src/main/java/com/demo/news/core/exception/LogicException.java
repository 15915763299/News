package com.demo.news.core.exception;

import com.demo.news.Code;
import lombok.Data;

/**
 * @author 尉涛
 * @date 2020-03-16 20:18
 **/
@Data
public class LogicException extends RuntimeException {

    private static final long serialVersionUID = 8785414384215222798L;

    private int code;
    private String msg;

    public LogicException() {
        this.code = Code.LOGIC_ERROR;
        this.msg = "未知异常";
    }

    public LogicException(String msg) {
        this.code = Code.LOGIC_ERROR;
        this.msg = msg;
    }

    public LogicException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
