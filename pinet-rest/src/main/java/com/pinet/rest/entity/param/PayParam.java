package com.pinet.rest.entity.param;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: xinjiang-shop-app
 * @description: 支付相关参数
 * @author: hhh
 * @create: 2022-12-20 14:15
 **/
@Data
public class PayParam {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * openId
     */
    private String openId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 支付金额
     */
    private BigDecimal payPrice;

    /**
     * 支付描述
     */
    private String payDesc;

    /**
     * 支付类型 1-订单支付 2-店帮主充值
     * 用于回调的时候
     */
    private Integer payType;

    /**
     * 支付密码 余额支付的时候使用
     */
    private String payPassWord;

    /**
     * 店铺ID
     */
    private Long shopId;

}
