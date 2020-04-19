package com.demo.news.Utils;

import java.util.List;
import java.util.regex.Pattern;

public class ValUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }

    public static boolean isNotIntegerBoolean(Integer boo) {
        return boo == null || (boo != 1 && boo != 0);
    }

    public static boolean isNotPrice(Float price) {
        return price == null || price <= 0;
    }

    public static boolean isDiffStr(String str1, String str2) {
        boolean b1 = (str1 == null || str1.length() == 0);
        boolean b2 = (str2 == null || str2.length() == 0);
        return (!b1 || !b2) && (b1 || b2 || !str1.equals(str2));
    }

    /**
     * 验证密码格式
     */
    public static String validatePassword(String password) {
        if (isEmpty(password)) {
            return "密码不能为空";
        } else if (password.length() < 6 || password.length() > 12) {
            return "密码长度为6~12位";
        } else if (!password.matches("^[A-Za-z0-9]+$")) {
            return "密码必须由数字或字母组成";
        } else {
            return null;
        }
    }

    private static final Pattern DATE_PA = Pattern.compile("((19|20)[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])");

    /**
     * 日期
     */
    public static boolean isNotDate(String date) {
        return !DATE_PA.matcher(date).matches();
    }

    private static final Pattern DATE_TIME_PA = Pattern.compile("((19|20)[0-9]{2})(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])"
            + "([01][0-9]|2[0-3])[0-5][0-9][0-5][0-9]");

    /**
     * 日期+时间
     */
    public static boolean isNotDateTime(String date) {
        return !DATE_TIME_PA.matcher(date).matches();
    }


}
