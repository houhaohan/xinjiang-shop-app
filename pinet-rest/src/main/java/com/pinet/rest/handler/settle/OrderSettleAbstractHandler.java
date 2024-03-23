package com.pinet.rest.handler.settle;

import com.pinet.rest.handler.order.ShippingFeeHandler;

/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-03-22 16:40
 */
public abstract class OrderSettleAbstractHandler extends ShippingFeeHandler implements OrderSettleHandler{

    protected OrderSetterContext context;

}
