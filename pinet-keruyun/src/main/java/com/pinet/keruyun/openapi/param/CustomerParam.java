package com.pinet.keruyun.openapi.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-05-09 15:22
 */
@Data
public class CustomerParam {

    @ApiModelProperty("手机号")
    private String mobile;
}
