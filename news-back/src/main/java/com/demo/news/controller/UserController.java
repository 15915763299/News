package com.demo.news.controller;

import com.demo.news.entity.LoginReq;
import com.demo.news.entity.LoginRes;
import com.demo.news.entity.RegisterReq;
import com.demo.news.entity.base.BaseRes;
import com.demo.news.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author 尉涛
 * @date 2020-03-16 19:36
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource(name = "UserServiceImpl")
    private UserService userService;

    @ApiOperation(value = "获取验证码")
    @RequestMapping(value = "/validateCode", method = RequestMethod.GET)
    @ResponseBody
    public BaseRes<String> getValidateCode(
            @RequestParam("phone")
            @NotNull(message = "【手机号】不能为空")
            @Length(min = 11, max = 11, message = "【手机号】长度不正确") String phone) throws Exception {
        String code = userService.getValidateCode(phone);
        return new BaseRes<>(code);
    }

    @ApiOperation(value = "注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public BaseRes register(@RequestBody @Valid RegisterReq req) throws Exception {
        userService.register(req);
        return new BaseRes();
    }

    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public BaseRes<LoginRes> login(@RequestBody @Valid LoginReq req) throws Exception {
        LoginRes res = userService.login(req);
        return new BaseRes<>(res);
    }


}
