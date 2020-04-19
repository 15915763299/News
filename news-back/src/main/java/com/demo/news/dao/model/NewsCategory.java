package com.demo.news.dao.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * 票据类型
 * 电子票据信息通过电子票据代码（省份代码+简码+年份）关联票据种类
 *
 * @author 尉迟涛
 */
@Data
@TableName(value = "R_News_category")
public class NewsCategory extends Model<NewsCategory> {

    private static final long serialVersionUID = -8705874111382275317L;

    @TableId(value = "newsId")
    private String newsId;
    @TableField(value = "categoryId")
    private String categoryId;

}
