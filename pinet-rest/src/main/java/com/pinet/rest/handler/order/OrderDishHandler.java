package com.pinet.rest.handler.order;

import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.request.CartOrderProductRequest;
import com.pinet.rest.entity.request.DirectOrderRequest;

/**
 * @description 购物车订单处理器
 * @author chengshuanghui
 * @data 2024-03-21 15:00
 */
public interface OrderDishHandler {

    /**
     * 直接购买处理方法
     * @param request
     * @return
     */
    public OrderProduct execute(DirectOrderRequest request);


    /**
     * 购物车处理方法
     * @param request
     * @return
     */
    public OrderProduct execute(CartOrderProductRequest request);
}
