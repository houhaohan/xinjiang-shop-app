package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DcOrderBizRequest {

    @ApiModelProperty(value = "点餐类型， TAKE_OUT(“TAKE_OUT”, “外带”), FOR_HERE(“FOR_HERE”, “堂食”)",required = true)
    private String dinnerType;

    @ApiModelProperty("打包费，单位分")
    private Long takeoutFee;

    @ApiModelProperty("餐具总价格，单位分")
    private Long tableWareFee;

    @ApiModelProperty(value = "取餐方式， TO_TABLE(“TO_TABLE”, “送餐到桌”), SELF_TAKE(“SELF_TAKE”, “台前自取”)",required = true)
    private String takeMealType;
}
