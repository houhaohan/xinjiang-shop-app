package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: xinjiang-shop-mini
 * @author: chengshuanghui
 * @date: 2024-03-17 16:21
 */
@Data
public class ComboDetailVo {
    @ApiModelProperty("商品Id")
    private Long shopProdId;

    @ApiModelProperty("套餐价格")
    private Long price;

    @ApiModelProperty("套餐市场价")
    private Long sellPrice;

    @ApiModelProperty("商品名称，逗号拼接")
    private String dishName;
}
