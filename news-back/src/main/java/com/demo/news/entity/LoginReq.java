package com.demo.news.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @author 尉涛
 * @date 2020-03-16 20:02
 **/
@Data
@ApiModel(description = "登录请求")
public class LoginReq {

    @ApiModelProperty(value = "手机号", required = true)
    @NotEmpty(message = "【手机号】不能为空")
    @Length(max = 11, message = "【手机号】长度不能超过【11】")
    private String phone;

    @ApiModelProperty(value = "密码", required = true)
    @NotEmpty(message = "【密码】不能为空")
    @Length(max = 100, message = "【密码】长度不能超过【100】")
    private String password;

}
