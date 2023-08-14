package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SnackOrderRefundDTO {
    @ApiModelProperty("创建订单时系统返回给开发者的开放平台订单id\n")
    private String orderId;
}
