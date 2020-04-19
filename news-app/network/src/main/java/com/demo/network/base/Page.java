package com.demo.network.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author 尉迟涛
 * create time : 2020/3/9 0:44
 * description :
 */
public class Page<T> {

    @SerializedName("records")
    @Expose
    public List<T> records;
    /**
     * 总页数
     */
    @SerializedName("total")
    @Expose
    public long total;
    /**
     * 页长
     */
    @SerializedName("size")
    @Expose
    public long size;
    /**
     * 当前页数
     */
    @SerializedName("current")
    @Expose
    public long current;
    /**
     * 总页数
     */
    @SerializedName("pages")
    @Expose
    public long pages;
}

//{
//    "code": 200,
//    "msg": "成功",
//    "data": {
//        "records": [...],
//        "total": 4,
//        "size": 2,
//        "current": 1,
//        "pages": 2
//    }
//}
