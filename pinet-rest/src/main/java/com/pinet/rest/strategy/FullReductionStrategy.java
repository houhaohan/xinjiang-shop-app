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
 * 满减券 策略
 * @author chengshuanghui
 */
public class FullReductionStrategy implements PromotionStrategy {
    private BigDecimal usePrice;
    private BigDecimal couponPrice;
    private String couponName;
    private List<OrderDiscount> orderDiscounts;

    public FullReductionStrategy(BigDecimal usePrice, BigDecimal couponPrice,String couponName) {
        this.usePrice = usePrice;
        this.couponPrice = couponPrice;
        this.couponName = couponName;
        this.orderDiscounts = new ArrayList<>();
    }

    @Override
    public List<OrderDiscount> apply(BigDecimal amount) {
        if(amount.compareTo(usePrice) > 0){
            OrderDiscount orderDiscount = new OrderDiscount();
            orderDiscount.setDiscountMsg(couponName);
            orderDiscount.setDiscountAmount(couponPrice);
            orderDiscount.setType(DiscountTypeEnum.COUPON.getCode());
            orderDiscounts.add(orderDiscount);
        }
        return orderDiscounts;
    }

    @Override
    public List<OrderDiscount> apply(List<OrderProductVo> orderProducts) {
        if(CollectionUtils.isEmpty(orderProducts)){
            return new ArrayList<>();
        }
        orderProducts.sort((o1, o2) -> o2.getProdPrice().compareTo(o1.getProdPrice()));
        BigDecimal couponPrice = this.couponPrice;
        for(OrderProductVo orderProduct : orderProducts){
            OrderDiscount orderDiscount = new OrderDiscount();
            orderDiscount.setType(DiscountTypeEnum.COUPON.getCode());
            if(BigDecimalUtil.ge(orderProduct.getProdPrice(),couponPrice)){
                orderDiscount.setDiscountAmount(couponPrice);
                orderDiscount.setDiscountMsg(orderProduct.getProdName());
                orderDiscounts.add(orderDiscount);
                break;
            }
            //优惠券金额 小于商品金额的情况
            //eg：优惠券5元，商品3元，共优惠3元
            couponPrice = BigDecimalUtil.subtract(couponPrice,orderProduct.getProdPrice());
            orderDiscount.setDiscountAmount(orderProduct.getProdPrice());
            orderDiscount.setDiscountMsg(orderProduct.getProdName());
            orderDiscounts.add(orderDiscount);
        }
        return orderDiscounts;
    }
}
