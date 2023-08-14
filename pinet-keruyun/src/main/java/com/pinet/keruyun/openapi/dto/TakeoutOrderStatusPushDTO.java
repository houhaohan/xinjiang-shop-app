package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TakeoutOrderStatusPushDTO {

    @ApiModelProperty("创建订单时系统返回给开发者的开放平台订单id")
    private String orderId;
    @ApiModelProperty("订单配送状态编码,1:待接单,2:待取货,3:配送中,4:已完成,5:已取消")
    private Integer deliveryStatus;
    @ApiModelProperty("订单配送状态编码,1:待接单,2:待取货,3:配送中,4:已完成,5:已取消")
    private Long time;
}
