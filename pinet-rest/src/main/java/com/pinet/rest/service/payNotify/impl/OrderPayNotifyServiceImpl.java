package com.pinet.rest.service.payNotify.impl;

import com.pinet.rest.entity.param.OrderPayNotifyParam;
import com.pinet.rest.service.IOrdersService;
import com.pinet.rest.service.payNotify.IPayNotifyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: xinjiang-shop-app
 * @description: 订单支付impl
 * @author: hhh
 * @create: 2023-06-13 14:06
 **/
@Service("order_pay_notify_service")
public class OrderPayNotifyServiceImpl implements IPayNotifyService {
    @Resource
    private IOrdersService ordersService;

    @Override
    public boolean payNotify(OrderPayNotifyParam orderPayNotifyParam) {
        return ordersService.orderPayNotify(orderPayNotifyParam);
    }
}
