package com.demo.c.news.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author 尉迟涛
 * create time : 2020/3/9 0:41
 * description :
 */
public class Category {
    @SerializedName("categoryId")
    @Expose
    public String categoryId;
    @SerializedName("categoryName")
    @Expose
    public String categoryName;
}
