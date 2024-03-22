package com.pinet.rest.handler.settle;

import com.pinet.rest.entity.Cart;

/**
 * @description: 菜品处理器
 * @author: chengshuanghui
 * @date: 2024-03-22 17:36
 */
public interface DishSettleHandler {
    /**
     * 购物车结算菜品处理方法
     */
    void handler(Cart cart);

    /**
     * 直接结算菜品处理方法
     */
    void handler();
}
