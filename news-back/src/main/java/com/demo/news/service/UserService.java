package com.demo.news.service;

import com.demo.news.entity.LoginReq;
import com.demo.news.entity.LoginRes;
import com.demo.news.entity.token.LoginUser;
import com.demo.news.entity.RegisterReq;

/**
 * @author 尉涛
 * @date 2020-03-16 20:02
 **/
public interface UserService {

    String getValidateCode(String phone) throws Exception;

    void register(RegisterReq req) throws Exception;

    LoginRes login(LoginReq req) throws Exception;

    LoginUser getLoginUser(String phone);

}
