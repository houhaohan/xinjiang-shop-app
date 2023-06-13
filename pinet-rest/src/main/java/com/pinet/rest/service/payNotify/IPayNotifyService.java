package com.pinet.rest.service.payNotify;


import com.pinet.rest.entity.param.OrderPayNotifyParam;

/**
 * @program: xinjiang-shop-zyt
 * @description: 支付回调业务处理service
 * @author: hhh
 * @create: 2023-04-17 09:53
 **/
public interface IPayNotifyService {
    /**
     * 支付回调业务处理
     * @return 是否成功
     */
    boolean payNotify(OrderPayNotifyParam orderPayNotifyParam);
}
