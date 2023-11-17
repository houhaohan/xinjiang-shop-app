package com.pinet.rest.service;

import com.imdada.open.platform.callback.internal.CallbackParam;
import com.imdada.open.platform.client.internal.req.order.AddOrderReq;
import com.imdada.open.platform.client.internal.resp.order.AddOrderResp;
import com.imdada.open.platform.exception.RpcException;
import com.pinet.rest.entity.Orders;

public interface IDaDaService {

    void callback(CallbackParam callbackParam) throws RpcException;

    /**
     * 同步订单状态
     * @param callbackParam
     */
    void syncOrderStatus(CallbackParam callbackParam) throws RpcException;

    /**
     * 创建订单
     * @param orders
     */
    AddOrderResp createOrder(Orders orders) throws RpcException;

    /**
     * 重新下单
     * @param orders
     * @return
     * @throws RpcException
     */
    AddOrderResp reAddOrder(Orders orders) throws RpcException;

    /**
     * 查询运费
     * @param req
     * @return
     * @throws RpcException
     */
    AddOrderResp queryDeliverFee(AddOrderReq req) throws RpcException;

    /**
     * 取消达达骑手订单
     * @param orderNo
     */
    void cancelOrder(Long orderNo) throws RpcException;


}
