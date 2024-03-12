package com.pinet.rest.handler;

import java.math.BigDecimal;

/**
 * @description: OrderHandler
 * @author: chengshuanghui
 * @date: 2024-03-09 10:22
 */
public interface OrderHandler {
    /**
     * 创建订单
     */
    void createOrder();

    /**
     * 基本数据校验
     */
    void checkData();

    /**
     * 配送费处理
     * @return
     */
    BigDecimal deliveryFeeHandler();

    /**
     * 限时活动处理
     */
    void promotionHandler();

    /**
     * 订单金额处理
     */
    void amountHandler();


    /**
     * 订单入库
     */
    void insertData();

    /**
     * 优惠卷处理
     */
    void couponHandler();

    /**
     * 下单成功处理
     */
    void successHandler();
}
