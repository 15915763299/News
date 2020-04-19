package com.demo.c.news.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author 尉迟涛
 * create time : 2020/3/9 0:41
 * description :
 */
public class News {

    @SerializedName("newsId")
    @Expose
    public String newsId;
    /**
     * 来源
     */
    @SerializedName("sourceId")
    @Expose
    public String sourceId;
    /**
     * 作者
     */
    @SerializedName("author")
    @Expose
    public String author;
    /**
     * 标题
     */
    @SerializedName("title")
    @Expose
    public String title;
    /**
     * 描述
     */
    @SerializedName("description")
    @Expose
    public String description;
    /**
     * 新闻链接
     */
    @SerializedName("newsUrl")
    @Expose
    public String newsUrl;
    /**
     * 图片链接
     */
    @SerializedName("imageUrl")
    @Expose
    public String imageUrl;
    /**
     * 创建时间
     */
    @SerializedName("createTime")
    @Expose
    public String createTime;

}
