package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: xinjiang-shop-mini
 * @author: chengshuanghui
 * @date: 2024-03-15 16:28
 */
@Data
public class CartComboDishSpecVo {
    @ApiModelProperty("购物车ID")
    private Long cartId;

    @ApiModelProperty("套餐商品ID")
    private Long shopProdId;

    @ApiModelProperty("店铺ID")
    private Long shopId;

    @ApiModelProperty("套餐内单品ID")
    private Long singleProdId;

    @ApiModelProperty("套餐内单品样式ID")
    private Long shopProdSpecId;

    @ApiModelProperty("套餐内单品样式名称")
    private String shopProdSpecName;

    @ApiModelProperty("加价")
    private BigDecimal addPrice = BigDecimal.ZERO;
}
