package com.demo.news.dao.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 区划
 *
 * @author 尉迟涛
 */
@Data
@TableName(value = "T_News")
public class News extends Model<News> {

    private static final long serialVersionUID = 2796342096449641022L;

    @TableId(value = "newsId")
    private String newsId;
    /**
     * 来源
     */
    @TableField(value = "sourceId")
    private String sourceId;
    /**
     * 作者
     */
    @TableField(value = "author")
    private String author;
    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;
    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;
    /**
     * 新闻链接
     */
    @TableField(value = "newsUrl")
    private String newsUrl;
    /**
     * 图片链接
     */
    @TableField(value = "imageUrl")
    private String imageUrl;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "createTime")
    private Date createTime;
}
