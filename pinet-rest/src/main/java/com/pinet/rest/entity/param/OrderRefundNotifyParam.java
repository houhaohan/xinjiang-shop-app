package com.pinet.rest.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: xinjiang-shop-app
 * @description: 订单退款回调param
 * @author: hhh
 * @create: 2023-01-05 14:09
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRefundNotifyParam {
    private Long refundNo;

    private String outTradeNo;
}
