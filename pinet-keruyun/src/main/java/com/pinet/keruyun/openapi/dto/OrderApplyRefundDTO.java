package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderApplyRefundDTO {
    @ApiModelProperty("创建订单时系统返回给开发者的开放平台订单id")
    private String orderId;
    @ApiModelProperty("申请退款原因")
    private String reason;
}
