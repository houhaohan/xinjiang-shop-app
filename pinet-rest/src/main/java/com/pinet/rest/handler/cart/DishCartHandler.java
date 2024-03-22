package com.pinet.rest.handler.cart;

import com.pinet.rest.entity.Cart;
import com.pinet.rest.entity.enums.CartStatusEnum;
import org.springframework.beans.BeanUtils;


/**
 * @description: xinjiang-shop-mini
 * @author: chengshuanghui
 * @date: 2024-03-18 14:42
 */
public abstract class DishCartHandler implements CartHandler {
    protected CartContext context;

    /**
     * 构建购物车信息
     * @return
     */
    protected Cart buildCartInfo(){
        Cart cart = new Cart();
        BeanUtils.copyProperties(context.request, cart);
        cart.setCartStatus(CartStatusEnum.NORMAL.getCode());
        cart.setCustomerId(context.customerId);
        cart.setDishId(context.shopProduct.getProdId());
        cart.setDishType(context.shopProduct.getDishType());
        cart.setUnit(context.shopProduct.getUnit());
        return cart;
    }

}
