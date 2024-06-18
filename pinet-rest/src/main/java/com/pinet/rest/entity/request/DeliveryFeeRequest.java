package com.pinet.rest.entity.request;


import lombok.Data;

/**
 * @description: 计算配送费的参数
 * @author: chengshuanghui
 * @date: 2024-06-18 17:43
 */
@Data

public class DeliveryFeeRequest {

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     * 配送距离
     */
    private Integer orderDistance;
    /**
     * 配送平台
     */
    private String deliveryPlatform;
    /**
     * 会员登记
     */
    private Integer vipLevel;
    /**
     * 免配送费的订单数量
     */
    private Integer orderCnt;

}
