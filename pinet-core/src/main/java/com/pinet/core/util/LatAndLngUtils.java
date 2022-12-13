package com.pinet.core.util;

import java.math.BigDecimal;

/**
 * @program: xinjiang-shop-app
 * @description: 经纬度工具类
 * @author: hhh
 * @create: 2022-12-02 15:27
 **/
public class LatAndLngUtils {
    /**
     * 默认地球半径  赤道半径(单位m)
     */
    private static final double EARTH_RADIUS = 6371000;

    /**
     * 转化为弧度(rad)
     * */
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    /**
     * @param lng1 第一点的精度
     * @param lat1 第一点的纬度
     * @param lng2 第二点的精度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位m  保留两位小数
     * */
    public static double getDistance(double lng1, double lat1, double lng2, double lat2) {

        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 100d) / 100d;
        return s;
    }

    /**
     * 根据经纬度，计算两点间的距离
     * @param longitudeFrom  第一个点的经度
     * @param latitudeFrom  第一个点的纬度
     * @param longitudeTo 第二个点的经度
     * @param latitudeTo  第二个点的纬度
     * @param accurate  保留小数点几位
     * @return 返回距离 单位千米
     */
    public static double getDistance(double longitudeFrom, double latitudeFrom, double longitudeTo, double latitudeTo,int accurate) {
        double distance = getDistance(longitudeFrom, latitudeFrom, longitudeTo, latitudeTo);
        if (accurate < 0) {
            throw new RuntimeException("精确度必须是正整数或零");
        }
        return new BigDecimal(distance).divide(new BigDecimal(1000),accurate, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
