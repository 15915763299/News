package com.demo.network.errorhandler;

import android.net.ParseException;

import com.demo.network.Code;
import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;

import retrofit2.HttpException;

/**
 * handleException传入的异常包括：
 * 服务端状态码非成功的情况，统一为ServerException
 * 本地抛出的异常，比如JsonParseException
 */
public class ExceptionHandle {


    static ResponseThrowable handleException(Throwable e) {
        ResponseThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponseThrowable(e, Code.Self.HTTP_ERROR);
            switch (httpException.code()) {
                case Code.Http.UNAUTHORIZED:
                case Code.Http.FORBIDDEN:
                case Code.Http.NOT_FOUND:
                case Code.Http.REQUEST_TIMEOUT:
                case Code.Http.GATEWAY_TIMEOUT:
                case Code.Http.INTERNAL_SERVER_ERROR:
                case Code.Http.BAD_GATEWAY:
                case Code.Http.SERVICE_UNAVAILABLE:
                default:
                    ex.message = "网络错误";
                    break;
            }
            return ex;
        } else if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            ex = new ResponseThrowable(resultException, resultException.code);
            ex.message = resultException.message;
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ResponseThrowable(e, Code.Self.PARSE_ERROR);
            ex.message = "解析错误";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponseThrowable(e, Code.Self.CONNECT_ERROR);
            ex.message = "连接失败";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponseThrowable(e, Code.Self.SSL_ERROR);
            ex.message = "证书验证失败";
            return ex;
        } else if (e instanceof ConnectTimeoutException) {
            ex = new ResponseThrowable(e, Code.Self.TIMEOUT_ERROR);
            ex.message = "连接超时";
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ResponseThrowable(e, Code.Self.TIMEOUT_ERROR);
            ex.message = "连接超时";
            return ex;
        } else {
            ex = new ResponseThrowable(e, Code.Self.UNKNOWN_ERROR);
            ex.message = "未知错误";
            return ex;
        }
    }

    public static class ResponseThrowable extends Exception {
        public int code;
        public String message;

        public ResponseThrowable(Throwable throwable, int code) {
            super(throwable);
            this.code = code;
        }

        public ResponseThrowable(ServerException exception) {
            this.code = exception.code;
            this.message = exception.message;
        }
    }

    public static class ServerException extends RuntimeException {
        public int code;
        public String message;

        public ServerException(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}

