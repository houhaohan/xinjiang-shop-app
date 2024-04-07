package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-04-07 18:02
 */
@Data
@ApiModel(description = "套餐明细")
public class SideDishGroupDTO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("小料数量")
    private Integer quantity;

    @ApiModelProperty("加价（小料单价），添加购物车接口不用传")
    private BigDecimal addPrice;

}
