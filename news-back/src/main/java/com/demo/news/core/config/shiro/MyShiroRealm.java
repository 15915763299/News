package com.demo.news.core.config.shiro;

import com.demo.news.Utils.Md5Utils;
import com.demo.news.entity.token.LoginUser;
import com.demo.news.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Description  : 身份校验核心类
 */

public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 认证信息.(身份验证)
     * Authentication 是用来验证用户身份
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        LoginUser loginUser = userService.getLoginUser(username);
        if (loginUser == null) {
            throw new UnknownAccountException("未找到用户");
        }
        //TODO 验证用户状态

        return new SimpleAuthenticationInfo(
                loginUser.getPhone(),       //账号
                loginUser.getPassword(),    //密码
                getName()                   //realm name
        );
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new SimpleAuthorizationInfo();
    }

    /**
     * 自定义 密码验证类
     */
    public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
        @Override
        public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
            UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

            Object tokenCredentials = encrypt(String.valueOf(token.getPassword()));
            Object accountCredentials = getCredentials(info);
            //将密码加密与系统加密后的密码校验，内容一致就返回true,不一致就返回false
            return equals(tokenCredentials, accountCredentials);
        }

        //将传进来密码加密方法
        private String encrypt(String data) {
            return Md5Utils.getMd5(data);//这里可以选择自己的密码验证方式 比如 md5或者sha256等
        }
    }

    @PostConstruct
    public void initCredentialsMatcher() {
        setCredentialsMatcher(new CustomCredentialsMatcher());
    }
}
