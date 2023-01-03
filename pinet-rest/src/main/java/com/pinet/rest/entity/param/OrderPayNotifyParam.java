package com.pinet.rest.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: xinjiang-shop-app
 * @description: 订单支付回调参数
 * @author: hhh
 * @create: 2023-01-03 14:06
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPayNotifyParam {
    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 三方订单号
     */
    private String outTradeNo;

    /**
     * 支付渠道
     */
    private String channelId;

}
