package com.demo.news.entity.token;

import com.demo.news.dao.model.User;
import lombok.Data;

/**
 * @author 尉涛
 * @date 2020-03-16 23:12
 **/
@Data
public class LoginUser {

    private String userId;
    private String phone;
    private String password;

    public LoginUser(User user) {
        this.userId = user.getUserId();
        this.phone = user.getPhone();
        this.password = user.getPassword();
    }
}
