package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class KryOrderDetailDTO {

    @ApiModelProperty("订单ID")
    private String orderId;
}
