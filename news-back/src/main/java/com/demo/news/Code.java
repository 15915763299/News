package com.demo.news;

/**
 * @author 尉涛
 * @date 2020-03-16 20:22
 **/
public class Code {
    public static final int SUCCESS = 2000;

    /**
     * 未知错误
     */
    public static final int CLIENT_UNKNOWN_ERROR = 4000;
    /**
     * 请求参数有误
     */
    public static final int PARAMS_ERROR = 4001;



    /**
     * 未知错误
     */
    public static final int SERVER_UNKNOWN_ERROR = 5000;
    /**
     * 逻辑异常
     */
    public static final int LOGIC_ERROR = 5001;


    /**
     * 无效的token
     */
    public static final int INVALID_TOKEN = 5002;
    /**
     * 验签异常
     */
    public static final int VALIDATE_SIGN_ERROR = 5003;



}
