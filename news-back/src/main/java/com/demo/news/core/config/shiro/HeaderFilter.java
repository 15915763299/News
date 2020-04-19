package com.demo.news.core.config.shiro;

import com.demo.news.Code;
import com.demo.news.Utils.JWTUtil;
import com.demo.news.Utils.Md5Utils;
import com.demo.news.Utils.ValUtil;
import com.demo.news.core.SpringContextHolder;
import com.demo.news.entity.base.BaseRes;
import com.demo.news.entity.token.LoginUser;
import com.demo.news.entity.token.TokenUser;
import com.demo.news.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HeaderFilter extends AccessControlFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 5分钟
    private final static long TIME_LIMIT = 5 * 60 * 1000;
    private RedisTemplate<Object, Object> redisTemplate;

    HeaderFilter(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        boolean isValid;

        //RequestWrapper requestWrapper = new RequestWrapper(httpServletRequest);
        //String body = requestWrapper.getBody();

        String timestamp = httpServletRequest.getHeader("timestamp");
        isValid = validateTimestamp(request, response, timestamp);
        if (!isValid) {
            return false;
        }

        String token = httpServletRequest.getHeader("token");
        isValid = validateToken(request, response, token);
        if (!isValid) {
            return false;
        }

        String sign = httpServletRequest.getHeader("sign");
        return validateSign(request, response, sign, timestamp, token);
    }

    /**
     * 验证时间戳
     */
    private boolean validateTimestamp(ServletRequest request, ServletResponse response, String timestamp) throws IOException {
        long t;
        try {
            t = Long.parseLong(timestamp);
        } catch (Exception e) {
            responseError(request, response, Code.VALIDATE_SIGN_ERROR, "签名时间格式不正确");
            return false;
        }
        if (System.currentTimeMillis() - t > TIME_LIMIT) {
            responseError(request, response, Code.VALIDATE_SIGN_ERROR, "签名已过期");
            return false;
        }
        return true;
    }

    /**
     * 验证token
     */
    private boolean validateToken(ServletRequest request, ServletResponse response, String token) throws IOException {
        TokenUser tokenUser = JWTUtil.getTokenUser(token);
        if (tokenUser == null || ValUtil.isEmpty(tokenUser.getPhone())) {
            responseError(request, response, Code.INVALID_TOKEN, "Token验证失败");
            return false;
        } else {
            UserService loginService = SpringContextHolder.getBean(UserService.class);
            LoginUser loginUser = loginService.getLoginUser(tokenUser.getPhone());

            if (loginUser == null) {
                responseError(request, response, Code.INVALID_TOKEN, "未找到用户");
                return false;
            } else {
                Boolean hasKey = redisTemplate.hasKey(loginUser.getUserId());
                if (hasKey == null || !hasKey) {
                    responseError(request, response, Code.INVALID_TOKEN, "登录已失效");
                    return false;
                } else {
                    String redisToken = (String) redisTemplate.opsForValue().get(loginUser.getUserId());
                    if (!token.equals(redisToken)) {
                        responseError(request, response, Code.INVALID_TOKEN, "登录已失效");
                        return false;
                    }
                }
                //TODO 验证用户状态
            }
            return true;
        }
    }

    /**
     * 验证签名
     */
    private boolean validateSign(ServletRequest request, ServletResponse response,
                                 String sign, String timestamp, String token) throws IOException {
        String genSign = Md5Utils.getMd5(timestamp + token);
        boolean result = genSign != null && genSign.equals(sign);
        if (!result) {
            responseError(request, response, Code.VALIDATE_SIGN_ERROR, "验签失败");
        }
        return result;
    }

    /**
     * 返回信息
     */
    private void responseError(ServletRequest request, ServletResponse response, int code, String errorMsg) throws IOException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        logger.error("request: " + httpServletRequest.getRequestURI() + ", code: " + code + ", errorMsg: " + errorMsg);

        response.setContentType("application/json; charset=UTF-8");
        BaseRes baseRes = new BaseRes(code, errorMsg);
        OutputStream os = response.getOutputStream();
        os.write(new ObjectMapper().writeValueAsString(baseRes).getBytes(StandardCharsets.UTF_8));
        os.flush();
        os.close();
    }

}
