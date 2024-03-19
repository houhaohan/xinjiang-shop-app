package com.pinet.rest.handler;


import com.pinet.rest.entity.Cart;

import java.util.List;

public interface CartHandler {

    /**
     * 新增购物车处理方法
     */
    public void handler();

    /**
     * 刷新购物车数量
     */
    public void refreshCart(Cart cart,Integer prodNum);

    /**
     * 清除购物车
     * @param ids 购物车 ID
     */
    public void clear(List<Long> ids);
}
