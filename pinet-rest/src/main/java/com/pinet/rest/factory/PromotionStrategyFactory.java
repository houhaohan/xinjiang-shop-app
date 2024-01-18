package com.pinet.rest.factory;

import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.Coupon;
import com.pinet.rest.strategy.DiscountStrategy;
import com.pinet.rest.strategy.FullReductionStrategy;
import com.pinet.rest.strategy.PromotionStrategy;

import java.math.BigDecimal;

public class PromotionStrategyFactory {
    private Coupon coupon;

    public PromotionStrategyFactory() {

    }
    public PromotionStrategyFactory(Coupon coupon) {
        this.coupon = coupon;
    }


    public PromotionStrategy create(){
        if(coupon.getType() == 1){
            return new FullReductionStrategy(coupon.getUsePrice(),coupon.getCouponPrice(),coupon.getName());
        }else if(coupon.getType() == 2){
            return new DiscountStrategy(BigDecimalUtil.multiply(new BigDecimal(coupon.getDiscount()),0.01),coupon.getName());
        }
        return null;
    }
}
