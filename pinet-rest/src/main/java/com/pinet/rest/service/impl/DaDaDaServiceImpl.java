package com.pinet.rest.service.impl;

import com.imdada.open.platform.callback.internal.CallbackParam;
import com.imdada.open.platform.callback.internal.DaDaCallbackStatusEnum;
import com.pinet.rest.entity.OrderLogistics;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.entity.enums.OrderStatusEnum;
import com.pinet.rest.service.IDaDaService;
import com.pinet.rest.service.IOrderLogisticsService;
import com.pinet.rest.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class DaDaDaServiceImpl implements IDaDaService {
    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private IOrderLogisticsService orderLogisticsService;

    @Override
    public void callback(CallbackParam callbackParam) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncOrderStatus(CallbackParam callbackParam) {
        //订单状态 10待付款   20已支付（已下单）  30商家制作中   40商品配送中   50商品已送达   90订单已退款     99订单取消   100订单完成
        Orders orders = ordersService.getById(callbackParam.getOrderId());
        OrderLogistics orderLogistics = orderLogisticsService.getByOrderId(Long.valueOf(callbackParam.getOrderId()));
        if(DaDaCallbackStatusEnum.WAIT_ACCEPT.getOrderStatusCode().equals(callbackParam.getOrderStatus())){
            //{"order_status":1,"cancel_reason":"","update_time":1694158211,"cancel_from":0,"signature":"d0c956211692ea68d11daf0d81bfdec0","dm_id":0,"is_finish_code":false,"order_id":"2131312311","client_id":"1526014050948743168"}
            orderLogistics = new OrderLogistics();
            orderLogistics.setOrderId(Long.valueOf(callbackParam.getOrderId()));
            orderLogistics.setUpdateTime(new Date());
            orderLogistics.setClientId(callbackParam.getClientId());
            orderLogistics.setOrderStatus(callbackParam.getOrderStatus());
            orderLogisticsService.save(orderLogistics);
        }else if(DaDaCallbackStatusEnum.WAIT_PICK.getOrderStatusCode().equals(callbackParam.getOrderStatus())){
            //已接单-待取货
            orderLogistics.setAcceptTime(new Date());
            orderLogistics.setUpdateTime(new Date());
            orderLogistics.setClientId(callbackParam.getClientId());
            orderLogistics.setOrderStatus(callbackParam.getOrderStatus());
            orderLogistics.setDmId(callbackParam.getDmId());
            orderLogistics.setDmName(callbackParam.getDmName());
            orderLogistics.setDmMobile(callbackParam.getDmMobile());
            orderLogistics.setFinishCode(callbackParam.getFinishCode());
            orderLogisticsService.updateById(orderLogistics);
            //{"order_status":2,"cancel_reason":"","update_time":1694162762,"cancel_from":0,"signature":"06d73ae4f630d405db45cf0dcae2fbc2","dm_id":18979081,"is_finish_code":false,"dm_name":"开放平台联调骑士","order_id":"2131312311","client_id":"1526014050948743168","dm_mobile":"17592724030"}
        }else if(DaDaCallbackStatusEnum.ARRIVE_SHOP.getOrderStatusCode().equals(callbackParam.getOrderStatus())){
            //已取货-待到店
            orderLogistics.setOrderStatus(callbackParam.getOrderStatus());
        }else if(DaDaCallbackStatusEnum.DELIVERING.getOrderStatusCode().equals(callbackParam.getOrderStatus())){
            orderLogistics.setOrderStatus(callbackParam.getOrderStatus());
        }else if(DaDaCallbackStatusEnum.HAD_CANCEL.getOrderStatusCode().equals(callbackParam.getOrderStatus())){
            //重新发单
        }else if(DaDaCallbackStatusEnum.HAD_COMPLETE.getOrderStatusCode().equals(callbackParam.getOrderStatus())){
            orderLogistics.setOrderStatus(callbackParam.getOrderStatus());


            orders.setOrderStatus(OrderStatusEnum.COMPLETE.getCode());
        }
    }
}
