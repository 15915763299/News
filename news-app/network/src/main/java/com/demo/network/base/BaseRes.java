package com.demo.network.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * https://newsapi.org/docs/endpoints/sources
 * https://newsapi.org/docs/errors
 */
public class BaseRes<T> {

    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("code")
    @Expose
    public Integer code;
    @SerializedName("data")
    @Expose
    public T data;

}
