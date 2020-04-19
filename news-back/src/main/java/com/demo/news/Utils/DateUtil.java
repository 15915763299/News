/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.demo.news.Utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author antelope
 */
public class DateUtil {

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date parseDate(String date) {
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdfDay.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 格式化日期
     *
     * @return
     */
    public static String format(Date date, String pattern) {
        DateFormat fmt = new SimpleDateFormat(pattern);
        return fmt.format(date);
    }

    /**
     * 把日期转换为Timestamp
     *
     * @param date
     * @return
     */
    public static Timestamp format(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 获取当天的结束时间 单位：毫秒
     */
    public static Long getTodayEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime().getTime();
    }

    /**
     * 获取当天剩余毫秒数
     */
    public static long getTodayRemainMillis() {
        Calendar calendar = Calendar.getInstance();
        return getTodayEndTime() - calendar.getTimeInMillis();
    }


}
