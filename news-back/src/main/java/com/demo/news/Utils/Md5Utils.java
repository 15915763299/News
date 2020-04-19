package com.demo.news.Utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * MD5工具类
 */
public class Md5Utils {

    private static MessageDigest md5 = null;

    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 用于获取一个String的md5值
     */
    public static String getMd5(String str) {
        if (md5 == null) {
            return str;
        }

        byte[] bs = md5.digest(str.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte x : bs) {
            if ((x & 0xff) >> 4 == 0) {
                sb.append("0").append(Integer.toHexString(x & 0xff));
            } else {
                sb.append(Integer.toHexString(x & 0xff));
            }
        }
        return sb.toString();
    }

//    public static String MD5Encode(String aData) throws Exception {
//        String resultString;
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            resultString = bytes2HexString(md.digest(aData.getBytes(StandardCharsets.UTF_8)));
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Exception("MD5运算失败");
//        }
//        return resultString;
//    }
//
//    private static String bytes2HexString(byte[] bytes) {
//        StringBuilder ret = new StringBuilder();
//        for (byte b : bytes) {
//            String hex = Integer.toHexString(b & 0xFF);
//            if (hex.length() == 1) {
//                hex = '0' + hex;
//            }
//            ret.append(hex.toUpperCase());
//        }
//        return ret.toString();
//    }

}
