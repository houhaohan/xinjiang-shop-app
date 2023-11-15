package com.pinet.rest.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.imdada.open.platform.callback.internal.CallbackParam;
import com.imdada.open.platform.callback.internal.DaDaCallbackStatusEnum;
import com.imdada.open.platform.client.internal.req.order.AddOrderReq;
import com.imdada.open.platform.client.internal.req.order.internal.ProductDetail;
import com.imdada.open.platform.client.internal.resp.order.AddOrderResp;
import com.imdada.open.platform.client.order.AddOrderClient;
import com.imdada.open.platform.client.order.QueryDeliverFeeAndAddOrderClient;
import com.imdada.open.platform.client.order.ReAddOrderClient;
import com.imdada.open.platform.config.Configuration;
import com.imdada.open.platform.exception.RpcException;
import com.pinet.rest.entity.OrderAddress;
import com.pinet.rest.entity.OrderLogistics;
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.entity.enums.OrderStatusEnum;
import com.pinet.rest.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class DaDaServiceImpl implements IDaDaService {
    @Autowired
    private IOrdersService ordersService;

    @Autowired
    private IOrderLogisticsService orderLogisticsService;

    @Autowired
    private IOrderProductService orderProductService;

    @Autowired
    private IOrderAddressService orderAddressService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void callback(CallbackParam callbackParam) throws RpcException {
        syncOrderStatus(callbackParam);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncOrderStatus(CallbackParam callbackParam) throws RpcException {
        //订单状态 10待付款   20已支付（已下单）  30商家制作中   40商品配送中   50商品已送达   90订单已退款     99订单取消   100订单完成
        Orders orders = ordersService.getById(callbackParam.getOrderId());
        OrderLogistics orderLogistics = orderLogisticsService.getByOrderId(Long.valueOf(callbackParam.getOrderId()));
        if(DaDaCallbackStatusEnum.WAIT_ACCEPT.getOrderStatusCode().equals(callbackParam.getOrderStatus())){
            orderLogistics = new OrderLogistics();
            orderLogistics.setOrderId(Long.valueOf(callbackParam.getOrderId()));
            orderLogistics.setUpdateTime(new Date());
            orderLogistics.setClientId(callbackParam.getClientId());
            orderLogistics.setOrderStatus(callbackParam.getOrderStatus());
            orderLogistics.setPlatform("dada");
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

            orders.setOrderStatus(OrderStatusEnum.ORDER_TAKE.getCode());
        }else if(DaDaCallbackStatusEnum.ARRIVE_SHOP.getOrderStatusCode().equals(callbackParam.getOrderStatus())){
            //已到店-待取货
            orderLogistics.setOrderStatus(callbackParam.getOrderStatus());

            orders.setOrderStatus(OrderStatusEnum.TO_SHOP.getCode());
        }else if(DaDaCallbackStatusEnum.DELIVERING.getOrderStatusCode().equals(callbackParam.getOrderStatus())){
            orderLogistics.setOrderStatus(callbackParam.getOrderStatus());
            orderLogistics.setFetchTime(new Date());
            orders.setOrderStatus(OrderStatusEnum.SEND_OUT.getCode());
        }else if(DaDaCallbackStatusEnum.HAD_CANCEL.getOrderStatusCode().equals(callbackParam.getOrderStatus())){
            orderLogistics.setCancelFrom(callbackParam.getCancelFrom());
            orderLogistics.setCancelReason(callbackParam.getCancelReason());
            orderLogistics.setCancelTime(new Date());
            //重新发单
            reAddOrder(orders);
        }else if(DaDaCallbackStatusEnum.HAD_COMPLETE.getOrderStatusCode().equals(callbackParam.getOrderStatus())){
            orderLogistics.setOrderStatus(callbackParam.getOrderStatus());
            orders.setOrderStatus(OrderStatusEnum.COMPLETE.getCode());
        }
        ordersService.updateById(orders);
    }

    @Override
    public AddOrderResp createOrder(Orders orders) throws RpcException {
        //{"deliverFee":13.0,"distance":1097.0,"fee":13.0,"insuranceFee":0.0,"tips":1.0}
        AddOrderReq req = addOrderReq(orders);
        AddOrderResp resp = AddOrderClient.execute(req);
        System.err.println(JSONObject.toJSONString(resp));
        return resp;
    }

    @Override
    public AddOrderResp reAddOrder(Orders orders) throws RpcException {
        //{"deliverFee":13.0,"distance":1097.0,"fee":13.0,"insuranceFee":0.0,"tips":1.0}
        AddOrderReq req = addOrderReq(orders);
        AddOrderResp resp = ReAddOrderClient.execute(req);
        System.out.println(JSONObject.toJSONString(resp));
        return resp;
    }

    @Override
    public AddOrderResp queryDeliverFee(AddOrderReq req) throws RpcException {
        return QueryDeliverFeeAndAddOrderClient.execute(req);
    }

    private AddOrderReq addOrderReq(Orders orders){
        List<OrderProduct> orderProducts = orderProductService.getByOrderId(orders.getId());
        List<ProductDetail> productDetails = new ArrayList<>(orderProducts.size());
        for(OrderProduct orderProduct : orderProducts){
            ProductDetail productDetail = ProductDetail.builder()
                    .skuName(orderProduct.getProdName())
                    .count(orderProduct.getProdNum().doubleValue())
                    .srcProductNo(orderProduct.getDishId())
                    .unit(orderProduct.getUnit())
                    .build();
            productDetails.add(productDetail);
        }

        int sum = orderProducts.stream().mapToInt(OrderProduct::getProdNum).sum();
        OrderAddress orderAddress = orderAddressService.getOrderAddress(orders.getId());
        return AddOrderReq.builder()
                .shopNo("f1b801be8af3483a")//677075-8531106
                .originId(String.valueOf(orders.getOrderNo()))
                //.cityCode("021")
                .cargoPrice(orders.getOrderPrice().doubleValue())
                .cargoNum(sum)
                .tips(0D)
                .info(orders.getRemark())
                .cargoWeight(0.5)
                .isUseInsurance(0)
                .prepay(0)
                .finishCodeNeeded(0)
                .cargoType(1)
                .invoiceTitle(null)
                .receiverName(orderAddress.getName())
                .receiverAddress(orderAddress.getAddress())
                .receiverPhone(orderAddress.getTel())
                .receiverLat(Double.valueOf(orderAddress.getLat()))
                .receiverLng(Double.valueOf(orderAddress.getLng()))
                .callback(Configuration.getInstance().getCallback())
                .pickUpPos(null)
                .productList(productDetails)
                .build();
    }
}
