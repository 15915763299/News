package com.demo.news.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author 尉涛
 * @date 2020-03-16 19:37
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "登录返回信息")
public class LoginRes {

    private String token;
    private String userName;
    private String phone;
    private String headPic;

}
