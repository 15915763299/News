package com.demo.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author 尉迟涛
 * create time : 2020/3/10 15:36
 * description :
 */
public class ConvertUtils {

    public static final long ONE_MINUTE = 60_000L;
    public static final long ONE_HOUR = 3_600_000L;
    public static final long ONE_DAY = 86_400_000L;
    public static final long ONE_WEEK = 604_800_000L;

    private static SimpleDateFormat sdf;

    /**
     * 转换时间为距离当前多久
     *
     * @param time yyyy-dd-MM hh:mm:ss  ex:2020-03-08 22:40:13
     * @return XX小时、天、年前
     */
    public static String pointTime2PassTime(String time) {
        if (EmptyUtils.isEmpty(time)) {
            return "";
        }
        if (sdf == null) {
            // hh是12小时制，HH是24小时制
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        }

        Date date;
        try {
            date = sdf.parse(time);
            return pointTime2PassTime(date);
        } catch (ParseException e) {
            return "";
        }
    }


    private static String pointTime2PassTime(Date date) {
        long delta = new Date().getTime() - date.getTime();
        if (delta < ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + "秒前";
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + "分钟前";
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + "小时前";
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + "天前";
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + "月前";
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + "年前";
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }


}
