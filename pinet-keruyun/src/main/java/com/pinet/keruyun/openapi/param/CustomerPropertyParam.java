package com.pinet.keruyun.openapi.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 会员资产查询参数
 * @author: chengshuanghui
 * @date: 2024-05-09 16:03
 */
@Data
public class CustomerPropertyParam {

    @ApiModelProperty("会员ID")
    private String customerId;

    @ApiModelProperty("店铺ID")
    private String shopId;
}
