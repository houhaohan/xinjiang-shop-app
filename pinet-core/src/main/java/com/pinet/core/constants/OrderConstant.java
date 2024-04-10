package com.pinet.core.constants;

import java.math.BigDecimal;

/**
 * @author chengshuanghui
 */
public class OrderConstant {

    /**
     * 1未支付
     */
    public static final Integer UNPAID = 1;

    /**
     * 2已支付
     */
    public static final Integer PAID = 2;

    /**
     * 外卖
     */
    public static final Integer TAKEAWAY = 1;

    /**
     * 自提
     */
    public static final Integer SELF_PICKUP = 2;

    /**
     * 退款状态 1-已到账
     */
    public static final Integer RECEIVED = 1;

    /**
     * 退款状态 2-未到账
     */
    public static final Integer NOT_RECEIVED = 2;

    /**
     * 单品打包费
     */
    public static final BigDecimal SINGLE_PACKAGE_FEE = BigDecimal.ONE;

    /**
     * 套餐打包费
     */
    public static final BigDecimal COMBO_PACKAGE_FEE = new BigDecimal("2");

    /**
     * 未支付订单自动取消时间，15分钟
     */
    public static final Long ORDER_AUTO_CANCEL_TIME = 15 * 60 * 1000L;

    /**
     * 订单提现税率
     */
    public static final Double WITHDRAW_RATE = 0.54;

}
