package com.pinet.rest.handler;

import com.pinet.rest.entity.OrderProduct;

/**
 * @description: xinjiang-shop-mini
 * @author: chengshuanghui
 * @date: 2024-03-18 14:42
 */
public abstract class DishCartHandler {


    /**
     * 根据购物车ID查询订单商品信息
     * @param cartId
     * @return
     */
    public OrderProduct getOrderProductByCartId(Long cartId, Long shopProdId, Integer prodNum, Integer orderType){
        return null;
    };
}
