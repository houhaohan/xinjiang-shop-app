package com.pinet.rest.entity.request;


import lombok.Data;

/**
 * @description: 计算配送费的参数
 * @author: chengshuanghui
 * @date: 2024-06-18 17:43
 */
@Data

public class DeliveryFeeRequest {

    private Integer orderType;
    private Long customerId;
    private Integer orderDistance;
    private String deliveryPlatform;

}
