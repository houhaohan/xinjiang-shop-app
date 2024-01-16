package com.pinet.core.util;

import java.math.BigDecimal;
import java.util.Arrays;

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
     * 多数之和
     * @param amounts
     * @return
     */
    public static BigDecimal sum(BigDecimal... amounts) {
        return Arrays.stream(amounts).reduce(BigDecimal.ZERO,BigDecimal::add);
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

    /**
     * 两数之积
     * @param unitPrice (单位元)
     * @param num
     * @return 单位分
     */
    public static Integer multiply(BigDecimal unitPrice, Integer num) {
        BigDecimal yuan = unitPrice.multiply(new BigDecimal(num));
        return yuanToFen(yuan);
    }

    /**
     * 两数之积
     * @param amount (单位元)
     * @param val 折扣
     * @return 单位元
     */
    public static BigDecimal multiply(BigDecimal amount, BigDecimal val) {
        return amount.multiply(val).setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 去掉最末尾的0
     * @param amount (单位元)
     * @return 单位元
     */
    public static String stripTrailingZeros(BigDecimal amount) {
        return amount.stripTrailingZeros().toPlainString();
    }

}
