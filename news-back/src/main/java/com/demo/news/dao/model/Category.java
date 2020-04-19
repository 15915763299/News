package com.demo.news.dao.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * 证书表
 *
 * @author 尉迟涛
 */
@Data
@TableName(value = "T_Category")
public class Category extends Model<Category> {

    private static final long serialVersionUID = 4673054740277723522L;

    @TableId(value = "categoryId")
    private String categoryId;
    /**
     * 名称
     */
    @TableField(value = "categoryName")
    private String categoryName;
}
