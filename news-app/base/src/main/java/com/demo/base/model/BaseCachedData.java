package com.demo.base.model;

import java.io.Serializable;

/**
 * 缓存的最小单位，给需要缓存的数据加上一个时间戳
 */
public class BaseCachedData<T> implements Serializable {

    public static final String TIME_NAME = "updateTimeInMills";
    public static final String DATA_NAME = "data";

    public long updateTimeInMills;
    public T data;
}
