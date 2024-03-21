package com.pinet.rest.strategy;

import com.pinet.rest.entity.OrderDiscount;
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.vo.OrderProductVo;

import java.math.BigDecimal;
import java.util.List;

public interface PromotionStrategy {

    /**
     * 优惠处理
     * @param amount 菜品金额
     * @return
     */
    List<OrderDiscount> apply(BigDecimal amount);

    /**
     * 指定商品优惠
     * @param orderProducts 优惠商品列表
     * @return
     */
    List<OrderDiscount> apply(List<OrderProduct> orderProducts);
}
