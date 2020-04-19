package com.demo.news.Utils;

import java.util.Random;
import java.util.UUID;

/**
 * @author 尉涛
 * @date 2020-03-16 21:40
 **/
public class SystemUtils {

    /**
     * 获取ID（32位）
     */
    public static String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    public static String defaultUserName() {
        return String.valueOf(System.currentTimeMillis()).substring(5);
    }


    private static char[] NO_USE_CHAR = {91, 92, 93, 94, 95, 96, 58, 59, 60, 61, 62, 63, 64};

    /**
     * 生成随机数字和字母
     */
    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() != length) {
            boolean flag = true;
            int a = (random.nextInt(75) + 48);
            for (char b : NO_USE_CHAR) {
                if (a == b) {
                    flag = false;
                }
            }
            if (flag) {
                char c = (char) a;
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String getRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() != length) {
            int a = random.nextInt(10);
            sb.append(a);
        }
        return sb.toString();
    }
}
