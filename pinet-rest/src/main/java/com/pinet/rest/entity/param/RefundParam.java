package com.pinet.rest.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: xinjiang-shop-app
 * @description: 退款param
 * @author: hhh
 * @create: 2023-01-04 14:39
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundParam {

    /**
     * 支付的总金额
     */
    private String totalFee;

    /**
     * 支付的时候生成的订单号
     */
    private String orderNo;

    /**
     * 退款记录的订单号
     */
    private String outRefundNo;

    /**
     * 退款金额
     */
    private String refundFee;

    /**
     * 退款记录id
     */
    private Long orderRefundId;

    /**
     * 下单的用户id
     */
    private Long customerId;

    /**
     * 店铺ID
     */
    private Long shopId;

}
