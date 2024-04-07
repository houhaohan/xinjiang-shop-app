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
    @ApiModelProperty("小料id")
    private Long id;

    @ApiModelProperty("加价（小料单价）")
    private BigDecimal addPrice;

    @ApiModelProperty("小料数量")
    private Integer quantity;
}
