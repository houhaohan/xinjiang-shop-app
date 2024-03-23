package com.pinet.rest.handler.cart;


import java.util.List;

public interface CartHandler {

    /**
     * 新增购物车处理方法
     */
    void handler();

    /**
     * 刷新购物车数量
     */
    void refreshCart(Long cartId, Integer prodNum);

    /**
     * 清除购物车
     * @param ids 购物车 ID
     */
    void clear(List<Long> ids);
}
