package com.pinet.rest.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.imdada.open.platform.callback.internal.CallbackParam;
import com.imdada.open.platform.callback.internal.DaDaCallbackStatusEnum;
import com.imdada.open.platform.client.internal.req.order.AddOrderReq;
import com.imdada.open.platform.client.internal.req.order.CancelOrderReq;
import com.imdada.open.platform.client.internal.req.order.internal.ProductDetail;
import com.imdada.open.platform.client.internal.resp.order.AddOrderResp;
import com.imdada.open.platform.client.internal.resp.order.CancelOrderResp;
import com.imdada.open.platform.client.order.AddOrderClient;
import com.imdada.open.platform.client.order.CancelOrderClient;
import com.imdada.open.platform.client.order.QueryDeliverFeeAndAddOrderClient;
import com.imdada.open.platform.client.order.ReAddOrderClient;
import com.imdada.open.platform.config.Configuration;
import com.imdada.open.platform.exception.RpcException;
import com.pinet.common.mq.util.JmsUtil;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.Environment;
import com.pinet.core.util.StringUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.enums.DeliveryPlatformEnum;
import com.pinet.rest.entity.enums.OrderStatusEnum;
import com.pinet.rest.mapper.OrdersMapper;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class DaDaServiceImpl implements IDaDaService {

    @Resource
    private OrdersMapper ordersMapper;

    @Autowired
    private IOrderLogisticsService orderLogisticsService;

    @Autowired
    private IOrderProductService orderProductService;

    @Autowired
    private IOrderAddressService orderAddressService;

    @Autowired
    private IShopService shopService;

    @Autowired
    private JmsUtil jmsUtil;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void callback(CallbackParam callbackParam) throws RpcException {
        syncOrderStatus(callbackParam);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncOrderStatus(CallbackParam callbackParam) throws RpcException {
        if(!Environment.isProd()){
            return;
        }
        Orders orders = ordersMapper.selectByOrderNo(Long.valueOf(callbackParam.getOrderId()));
        log.info("~~~~~~~~~~~~~~~~~~~~dada回调查出订单数据{}",JSONObject.toJSONString(orders));
        OrderLogistics orderLogistics = orderLogisticsService.getByOrderId(Long.valueOf(callbackParam.getOrderId()));
        if(DaDaCallbackStatusEnum.WAIT_ACCEPT.getOrderStatusCode().equals(callbackParam.getOrderStatus())){
            orderLogistics = new OrderLogistics();
            orderLogistics.setOrderId(Long.valueOf(callbackParam.getOrderId()));
            orderLogistics.setUpdateTime(new Date());
            orderLogistics.setClientId(callbackParam.getClientId());
            orderLogistics.setOrderStatus(callbackParam.getOrderStatus());
            orderLogistics.setPlatform("dada");
            orderLogistics.setSignature(callbackParam.getSignature());
            orderLogistics.setDistance(orders.getOrderDistance().doubleValue());
            orderLogistics.setFee(0d);
            orderLogistics.setDeliverFee(0d);
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
            orderLogisticsService.updateById(orderLogistics);

            orders.setOrderStatus(OrderStatusEnum.TO_SHOP.getCode());
        }else if(DaDaCallbackStatusEnum.DELIVERING.getOrderStatusCode().equals(callbackParam.getOrderStatus())){
            orderLogistics.setOrderStatus(callbackParam.getOrderStatus());
            orderLogistics.setFetchTime(new Date());
            orderLogisticsService.updateById(orderLogistics);

            orders.setOrderStatus(OrderStatusEnum.SEND_OUT.getCode());
        }else if(DaDaCallbackStatusEnum.HAD_CANCEL.getOrderStatusCode().equals(callbackParam.getOrderStatus())){
            orderLogistics.setCancelFrom(callbackParam.getCancelFrom());
            orderLogistics.setCancelReason(callbackParam.getCancelReason());
            orderLogistics.setCancelTime(new Date());
            orderLogisticsService.updateById(orderLogistics);

            jmsUtil.sendMsgQueue(QueueConstants.DELIVERY_ORDER_CANCEL_SMS,String.valueOf(orders.getId()));
            if(Objects.equals(orders.getOrderStatus(),OrderStatusEnum.REFUND.getCode())){
                return;
            }
            orders.setOrderStatus(OrderStatusEnum.SEND_OUT.getCode());
            //重新发单
            //reAddOrder(orders);
        }else if(DaDaCallbackStatusEnum.HAD_COMPLETE.getOrderStatusCode().equals(callbackParam.getOrderStatus())){
            orderLogistics.setOrderStatus(callbackParam.getOrderStatus());
            orderLogistics.setFinishTime(new Date());
            orderLogisticsService.updateById(orderLogistics);

            orders.setOrderStatus(OrderStatusEnum.COMPLETE.getCode());
        }
        ordersMapper.updateById(orders);
    }

    @Override
    public AddOrderResp createOrder(Orders orders) throws RpcException {
        if(!Environment.isProd()){
            return null;
        }
        if(StringUtil.isBlank(orders.getKryOrderNo())){
            throw new PinetException("客如云单号为空,订单ID======》"+orders.getId());
        }
        Shop shop = shopService.getById(orders.getShopId());
        if(shop.getSupportDelivery() == 0){
            throw new PinetException("该门店暂不支持外卖订单");
        }
        if(DeliveryPlatformEnum.ZPS.getCode().equals(shop.getDeliveryPlatform())){
            orders.setOrderStatus(OrderStatusEnum.MAKE.getCode());
            return null;
        }
        AddOrderReq req = addOrderReq(orders);
        AddOrderResp resp = AddOrderClient.execute(req);
        return resp;
    }

    @Override
    public AddOrderResp reAddOrder(Orders orders) throws RpcException {
        if(!Environment.isProd()){
            return null;
        }
        AddOrderReq req = addOrderReq(orders);
        AddOrderResp resp = ReAddOrderClient.execute(req);
        return resp;
    }

    @Override
    public AddOrderResp queryDeliverFee(AddOrderReq req) throws RpcException {
        if(!Environment.isProd()){
            return new AddOrderResp();
        }
        return QueryDeliverFeeAndAddOrderClient.queryDeliverFee(req);
    }

    @Override
    public void cancelOrder(Long orderNo) throws RpcException {
        if(!Environment.isProd()){
            return;
        }
        CancelOrderReq req = CancelOrderReq.builder()
                .cancelReasonId(10000)
                .cancelReason("其他")
                .orderId(String.valueOf(orderNo))
                .build();
        CancelOrderResp resp = CancelOrderClient.execute(req);

        OrderLogistics orderLogistics = orderLogisticsService.getByOrderId(orderNo);
        if(orderLogistics == null){
            return;
        }
        orderLogistics.setCancelReason("其他");
        orderLogistics.setCancelFrom(2);
        orderLogistics.setCancelTime(new Date());
        orderLogistics.setDeductFee(resp.getDeductFee());
        orderLogisticsService.updateById(orderLogistics);
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
                .shopNo(getDeliveryShopNo(orders.getShopId()))
                .originId(String.valueOf(orders.getOrderNo()))
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

    /**
     * 获取达达的店铺编码
     * @param shopId
     * @return
     */
    private String getDeliveryShopNo(Long shopId){
        Shop shop = shopService.getById(shopId);
        return shop.getDeliveryShopNo();
    }
}
