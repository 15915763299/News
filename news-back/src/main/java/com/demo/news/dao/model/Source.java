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
@TableName(value = "T_Source")
public class Source extends Model<Source> {

    private static final long serialVersionUID = 8408143995937852723L;

    @TableId(value = "sourceId")
    private String sourceId;
    /**
     * 名称
     */
    @TableField(value = "sourceName")
    private String sourceName;
}
