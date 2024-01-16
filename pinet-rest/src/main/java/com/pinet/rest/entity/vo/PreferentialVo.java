package com.pinet.rest.entity.vo;

import com.pinet.rest.entity.OrderDiscount;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PreferentialVo {

    /**
     * 优惠明细
     */
    private List<OrderDiscount> orderDiscounts;

    /**
     * 订单商品总金额
     */
    private BigDecimal orderProductPrice;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 优惠后金额
     */
    private BigDecimal productDiscountAmount;
}
