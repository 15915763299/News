package com.demo.news.service.impl;

import com.demo.news.Utils.JWTUtil;
import com.demo.news.Utils.SystemUtils;
import com.demo.news.Utils.ValUtil;
import com.demo.news.core.exception.LogicException;
import com.demo.news.dao.UserMapper;
import com.demo.news.dao.model.User;
import com.demo.news.entity.LoginReq;
import com.demo.news.entity.LoginRes;
import com.demo.news.entity.token.LoginUser;
import com.demo.news.entity.RegisterReq;
import com.demo.news.service.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 尉涛
 * @date 2020-03-16 20:05
 **/
@Service("UserServiceImpl")
public class UserServiceImpl implements UserService {

    private static final long LOGIN_EXPIRE_TIME = 24 * 60 * 60 * 1000;
    private static final long CODE_EXPIRE_TIME = 5 * 60 * 1000;

    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;


    @Override
    public String getValidateCode(String phone) throws Exception {
        String code = SystemUtils.getRandomNumber(6);
        //5分钟有效期
        redisTemplate.opsForValue().set(phone, code, CODE_EXPIRE_TIME, TimeUnit.SECONDS);
        return code;
    }

    @Override
    public void register(RegisterReq req) throws Exception {
        String result = (String) redisTemplate.opsForValue().get(req.getPhone());
        if (ValUtil.isEmpty(result) || !result.equals(req.getCode())) {
            throw new LogicException("验证码校验失败");
        }

        HashMap<String, Object> params = new HashMap<>(1);
        params.put("phone", req.getPhone());

        List<User> users = userMapper.selectByMap(params);
        if (users.size() > 0) {
            throw new LogicException("用户已存在");
        }

        /*
         * 同一个密码加密结果不一样，解密却能正确解密
         * https://www.cnblogs.com/qianjinyan/p/10636404.html
         */
        String encryptPwd = BCrypt.hashpw(req.getPassword(), BCrypt.gensalt());

        User user = new User();
        user.setUserId(SystemUtils.generateId());
        user.setCreateTime(new Date());
        user.setPassword(encryptPwd);
        user.setPhone(req.getPhone());
        user.setUserName(SystemUtils.defaultUserName());
        userMapper.insert(user);
    }

    @Override
    public LoginRes login(LoginReq req) throws Exception {
        User user = getUser(req.getPhone());

        if (!BCrypt.checkpw(req.getPassword(), user.getPassword())) {
            throw new LogicException("用户名/密码不正确");
        }

        String token = JWTUtil.sign(user.getPhone(), user.getPassword());
        //一天有效期
        redisTemplate.opsForValue().set(user.getUserId(), token, LOGIN_EXPIRE_TIME, TimeUnit.SECONDS);

        LoginRes res = new LoginRes();
        res.setPhone(user.getPhone());
        res.setUserName(user.getUserName());
        res.setHeadPic(user.getHeadPic());
        res.setToken(token);
        return res;
    }

    /**
     * 获取用户
     */
    private User getUser(String phone) throws Exception {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("phone", phone);

        List<User> users = userMapper.selectByMap(params);
        if (users.size() == 0) {
            throw new LogicException("用户不存在");
        } else if (users.size() > 1) {
            throw new LogicException("存在相同用户，请联系管理员处理");
        }
        return users.get(0);
    }

    /**
     * 获取LoginUser用户验证token
     */
    @Override
    public LoginUser getLoginUser(String phone) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("phone", phone);

        List<User> users = userMapper.selectByMap(params);
        if (users.size() > 0) {
            return new LoginUser(users.get(0));
        }
        return null;
    }
}
