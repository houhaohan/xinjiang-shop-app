package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderStrategyRequest {

    @ApiModelProperty("是否校验菜品沽清 * validateDishStock = true，校验菜品沽清状态，若沽清，中断下单流程 * validateDishStock = false，不校验菜品沽清状态 */不传默认为true")
    private Boolean validateDishStock;
}
