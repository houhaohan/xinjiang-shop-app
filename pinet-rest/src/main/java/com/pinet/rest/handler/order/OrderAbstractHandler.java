package com.pinet.rest.handler.order;



import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.entity.enums.OrderStatusEnum;

import java.util.Date;

public abstract class OrderAbstractHandler implements OrderHandler{
    protected OrderContext context;

    public Orders buildOrder(){
        Long userId = context.customerId;
        Orders order = new Orders();
        order.setOrderNo(IdUtil.getSnowflake().nextId());
        order.setOrderType(context.request.getOrderType());
        order.setOrderStatus(OrderStatusEnum.NOT_PAY.getCode());
        order.setOrderSource(context.request.getOrderSource());
        order.setCustomerId(userId);
        order.setShopId(context.request.getShopId());
        order.setKryShopId(context.shop.getKryShopId());
        order.setShopName(context.shop.getShopName());
        order.setShippingFee(shippingFeeRule());
        Date now = new Date();
        order.setEstimateArrivalStartTime(DateUtil.offsetHour(now, 1));
        order.setEstimateArrivalEndTime(DateUtil.offsetMinute(now, 90));
        order.setOrderDistance(context.distance.intValue());
        order.setRemark(context.request.getRemark());
        order.setShareId(context.request.getShareId());
        order.setCustomerCouponId(context.request.getCustomerCouponId());
        return order;
    }


}
