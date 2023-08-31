package com.pinet.core.util;

import java.math.BigDecimal;

public class BigDecimalUtil {

    //分转元
    public static BigDecimal fenToYuan(Long amount) {
        return new BigDecimal(String.valueOf(amount))
                .multiply(new BigDecimal("0.01"))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static Integer yuanToFen(BigDecimal amount) {
        return amount.multiply(new BigDecimal("100")).intValue();
    }

    public static Long yuan2Fen(BigDecimal amount) {
        return amount.multiply(new BigDecimal("100")).longValue();
    }

    public static void main(String[] args) {
        BigDecimal bigDecimal = fenToYuan(1478L);
        System.out.println(bigDecimal);
    }
}
