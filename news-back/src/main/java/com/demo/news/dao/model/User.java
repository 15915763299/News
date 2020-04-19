package com.demo.news.dao.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 印章信息
 *
 * @author 尉迟涛
 */
@Data
@TableName(value = "S_User")
public class User extends Model<User> {

    private static final long serialVersionUID = -6653641141583607829L;

    @TableId(value = "userId")
    private String userId;
    /**
     * 手机号/账号
     */
    @TableField(value = "phone")
    private String phone;
    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "createTime")
    private Date createTime;
    /**
     * 用户名
     */
    @TableField(value = "userName")
    private String userName;
    /**
     * 头像
     */
    @TableField(value = "headPic")
    private String headPic;
}
