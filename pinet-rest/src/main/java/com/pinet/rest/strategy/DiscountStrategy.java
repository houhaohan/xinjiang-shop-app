package com.pinet.rest.strategy;

import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.OrderDiscount;
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.enums.DiscountTypeEnum;
import com.pinet.rest.entity.vo.OrderProductVo;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 折扣券策略
 */
public class DiscountStrategy implements PromotionStrategy {
    /**
     * 折扣 eg: 0.85
     */
    private BigDecimal discount;
    private String couponName;
    private List<OrderDiscount> orderDiscounts;

    public DiscountStrategy(BigDecimal discount,String couponName) {
        this.discount = discount;
        this.couponName = couponName;
        this.orderDiscounts = new ArrayList<>();
    }

    @Override
    public List<OrderDiscount> apply(BigDecimal amount) {
        BigDecimal discountAmount = BigDecimalUtil.multiply(amount, discount);
        OrderDiscount orderDiscount = new OrderDiscount();
        orderDiscount.setDiscountMsg(couponName);
        orderDiscount.setDiscountAmount(BigDecimalUtil.subtract(amount,discountAmount));
        orderDiscount.setType(DiscountTypeEnum.COUPON.getCode());
        orderDiscounts.add(orderDiscount);
        return orderDiscounts;
    }

    @Override
    public List<OrderDiscount> apply(List<OrderProduct> orderProducts) {
        if(CollectionUtils.isEmpty(orderProducts)){
            return new ArrayList<>();
        }
        for(OrderProduct orderProduct : orderProducts){
            BigDecimal discountAmount = BigDecimalUtil.multiply(orderProduct.getProdPrice(), discount);
            OrderDiscount orderDiscount = new OrderDiscount();
            orderDiscount.setDiscountMsg(orderProduct.getProdName());
            orderDiscount.setDiscountAmount(BigDecimalUtil.subtract(orderProduct.getProdPrice(),discountAmount));
            orderDiscount.setType(DiscountTypeEnum.COUPON.getCode());
            orderDiscounts.add(orderDiscount);
        }

        return orderDiscounts;
    }
}
