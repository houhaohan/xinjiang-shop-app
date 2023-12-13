package com.pinet.core.util;

import java.math.BigDecimal;

public class BigDecimalUtil {

    //分转元
    public static BigDecimal fenToYuan(Long amount) {
        return new BigDecimal(String.valueOf(amount))
                .multiply(new BigDecimal("0.01"))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 元转分，返回Integer
     * @param amount
     * @return
     */
    public static Integer yuanToFen(BigDecimal amount) {
        return amount.multiply(new BigDecimal("100")).intValue();
    }

    /**
     * 元转分，返回Long
     * @param amount
     * @return
     */
    public static Long yuan2Fen(BigDecimal amount) {
        return amount.multiply(new BigDecimal("100")).longValue();
    }

    /**
     * 元转分,返回String
     * @param amount
     * @return string
     */
    public static String yuan2FenStr(BigDecimal amount) {
        return amount.multiply(new BigDecimal("100"))
                .stripTrailingZeros()
                .toPlainString();
    }


    /**
     * 两数之和
     * @param amount1
     * @param amount2
     * @return
     */
    public static BigDecimal sum(BigDecimal amount1,BigDecimal amount2) {
        return amount1.add(amount2);
    }
    /**
     * 两数之差
     * @param amount1
     * @param amount2
     * @return
     */
    public static BigDecimal subtract(BigDecimal amount1,BigDecimal amount2) {
        return amount1.subtract(amount2);
    }

}
