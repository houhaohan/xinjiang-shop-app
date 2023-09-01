package com.pinet.keruyun.openapi.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderCreateVO {

    @ApiModelProperty("开放平台订单id")
    private String orderId;
    @ApiModelProperty("订单id")
    private Long tradeId;
    @ApiModelProperty("序列号")
    private String serialNumber;
    @ApiModelProperty("自定义取餐号")
    private String pickUpNumber;
}
