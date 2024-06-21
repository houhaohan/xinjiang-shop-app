package com.pinet.rest.handler.settle;

import cn.hutool.core.date.DateUtil;
import com.pinet.rest.entity.request.DeliveryFeeRequest;
import com.pinet.rest.handler.order.ShippingFeeHandler;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-03-22 16:40
 */
public abstract class OrderSettleAbstractHandler extends ShippingFeeHandler implements OrderSettleHandler{

    protected OrderSetterContext context;


    public BigDecimal calculateDeliveryFee() {
        //配送费
        DeliveryFeeRequest request = new DeliveryFeeRequest();
        request.setDeliveryPlatform(context.deliveryPlatform);
        request.setOrderDistance(context.distance.intValue());
        request.setOrderType(context.dishSettleContext.request.getOrderType());

        Integer vipLevel = context.vipUserService.getLevelByCustomerId(context.userId);
        request.setVipLevel(vipLevel);
        //查询本周是否有免配送费的单
        Date beginOfWeek = DateUtil.beginOfWeek(new Date()).toJdkDate();
        Integer cnt = context.ordersMapper.getFreeDeliveryFeeCount(beginOfWeek);
        request.setOrderCnt(cnt);
        return super.calculateDeliveryFee(request);
    }
}
