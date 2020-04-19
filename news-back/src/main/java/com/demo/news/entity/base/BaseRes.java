package com.demo.news.entity.base;

import com.demo.news.Code;
import lombok.Data;

/**
 * @author 尉涛
 * @date 2020-03-08 20:49
 **/
@Data
public class BaseRes<T> {

    private int code;
    private String msg;
    private T data;

    public BaseRes() {
        this.code = Code.SUCCESS;
        this.msg = "成功";
    }

    public BaseRes(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseRes(T data) {
        this.data = data;
        this.code = Code.SUCCESS;
        this.msg = "成功";
    }
}
