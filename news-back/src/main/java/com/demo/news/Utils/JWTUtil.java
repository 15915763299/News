package com.demo.news.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.demo.news.entity.token.TokenUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class JWTUtil {

    private static final Logger log = LoggerFactory.getLogger(JWTUtil.class);
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;
    public static final String[] SOCKET_CLAIMS = {"phone"};

    /**
     * 生成签名,EXPIRE_TIME毫秒后过期
     */
    public static String sign(String phone, String password) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        log.info("普通用户名和密码生成Socket Token签名，Token超时时间=" + DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));
        Algorithm algorithm = Algorithm.HMAC256(password);
        // 附带username信息
        String token = JWT.create()
                .withClaim(SOCKET_CLAIMS[0], phone)
                .withExpiresAt(date)
                .sign(algorithm);
        log.info("普通用户名和密码生成Socket Token签名，token=" + token);
        return token;
    }

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verifySocket(String token, String secret) {
        try {
            log.info("校验token是否正确, token=" + token);
            DecodedJWT jwt1 = JWT.decode(token);
            String phone = jwt1.getClaim(SOCKET_CLAIMS[0]).asString();

            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(SOCKET_CLAIMS[0], phone)
                    .build();
            /*DecodedJWT jwt =*/
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            log.error("校验token失败", exception);
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     */
    public static TokenUser getTokenUser(String token) {
        if (ValUtil.isEmpty(token)) {
            return null;
        }
        try {
            TokenUser result = new TokenUser();
            DecodedJWT jwt = JWT.decode(token);
            result.setPhone(jwt.getClaim(SOCKET_CLAIMS[0]).asString());
            return result;
        } catch (JWTDecodeException e) {
            return null;
        }
    }

//    /**
//     * 获得token中的信息无需secret解密也能获得
//     */
//    public static String getSocketParams(String token, String paramName) {
//        try {
//            DecodedJWT jwt = JWT.decode(token);
//            return jwt.getClaim(paramName).asString();
//        } catch (JWTDecodeException e) {
//            return null;
//        }
//    }

}
