package com.pinet.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtil {
    public static final String YYYY_MM_DD_HHMMSS_SDF = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_SDF = "yyyy-MM-dd";
    public static final String YYYYMMDD_SDF = "yyyyMMdd";

    public DateUtil() {
    }

    /**
     * 获得系统的时间，单位为毫秒,转换为妙
     *
     * @return 秒
     */
    public static long getNowTime() {
        return System.currentTimeMillis() / 1000L;
    }

    /**
     * 获取系统时间，单位为天
     *
     * @return 天
     */
    public static long getNowDay() {
        return (System.currentTimeMillis() / (1000 * 60 * 60 * 12));
    }

    public static String convertDate2String(Date date, String format) {
        return (new SimpleDateFormat(format)).format(date);
    }

    public static int dateToTimestamp(String dateString) {
        Date date = convertString2Date(dateString, "yyyy-MM-dd HH:mm:ss");
        String timestamp = String.valueOf(date.getTime() / 1000L);
        return Integer.parseInt(timestamp);
    }

    public static Date convertString2Date(String str, String format) {
        if (str != null && !"".equals(str)) {
            Calendar calendar = Calendar.getInstance();

            try {
                calendar.setTime((new SimpleDateFormat(format)).parse(str.trim()));
            } catch (ParseException var4) {
                throw new RuntimeException(var4);
            }

            return calendar.getTime();
        } else {
            return null;
        }
    }

    public static Date addDay(Date date, int day) {
        if (date == null) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(5, day);
            return calendar.getTime();
        }
    }

    public static Date addHour(Date date, int hour) {
        if (date == null) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(11, hour);
            return calendar.getTime();
        }
    }

    public static int getHour(Date date) {
        if (date == null) {
            return 0;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(11);
        }
    }

    public static int dateDiff(Date stateDate, Date endDate) {
        return (int) ((endDate.getTime() - stateDate.getTime()) / 1000L);
    }
}