package com.pinet.rest.service;

import com.pinet.rest.entity.param.PayParam;

/**
 * @program: xinjiang-shop-app
 * @description: 支付
 * @author: hhh
 * @create: 2022-12-20 13:47
 **/
public interface IPayService {
    /**
     * 支付接口 获取返回给前端唤醒的支付的具体参数
     * @param param p
     * @return o
     */
    Object pay(PayParam param);

    /**
     * 获取支付渠道名称
     * @return 渠道名称
     */
    String getPayName();
}
