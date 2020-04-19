package com.demo.network;

/**
 * @author 尉迟涛
 * create time : 2020/3/10 10:38
 * description :
 */
public class Code {

    /**
     * http错误码
     */
    public static class Http {
        public static final int UNAUTHORIZED = 401;
        public static final int FORBIDDEN = 403;
        public static final int NOT_FOUND = 404;
        public static final int REQUEST_TIMEOUT = 408;
        public static final int INTERNAL_SERVER_ERROR = 500;
        public static final int BAD_GATEWAY = 502;
        public static final int SERVICE_UNAVAILABLE = 503;
        public static final int GATEWAY_TIMEOUT = 504;
    }

    /**
     * 4开头：App端异常
     */
    public static class Self {
        /**
         * 未知错误
         */
        public static final int UNKNOWN_ERROR = 4000;
        /**
         * http错误
         */
        public static final int HTTP_ERROR = 4001;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 4002;
        /**
         * 连接异常
         */
        public static final int CONNECT_ERROR = 4003;
        /**
         * 证书异常
         */
        public static final int SSL_ERROR = 4004;
        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 4005;
    }

    /**
     * 5开头：与后台协商的错误码
     */
    public static class Server {
        public static final int SUCCESS = 2000;

        /**
         * 未知错误
         */
        public static final int UNKNOWN_ERROR = 5000;
        /**
         * 逻辑异常
         */
        public static final int LOGIC_ERROR = 5001;
    }
}
