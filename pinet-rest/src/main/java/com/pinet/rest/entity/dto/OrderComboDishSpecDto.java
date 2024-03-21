package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-03-20 09:59
 */
@Data
public class OrderComboDishSpecDto {

    @ApiModelProperty("样式id")
    private Long shopProdSpecId;

    @ApiModelProperty("样式名称")
    private String shopProdSpecName;

    @ApiModelProperty("加价")
    private BigDecimal addPrice;
}
