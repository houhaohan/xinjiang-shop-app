package com.pinet.rest.handler.order;

import java.math.BigDecimal;

public interface OrderHandler {

    /**
     * 创建订单
     */
    void create(OrderContext context);

    /**
     * 配送费计算规则
     */
    BigDecimal shippingFeeRule();


}
