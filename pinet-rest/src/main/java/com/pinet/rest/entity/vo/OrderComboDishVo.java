package com.pinet.rest.entity.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderComboDishVo {

    @ApiModelProperty("套餐 ID")
    private Long shopProdId;

//    @ApiModelProperty("套餐 名称")
//    private String prodName;

    @ApiModelProperty("单品规格ID")
    private Long shopProdSpecId;

    @ApiModelProperty("单品规格名称")
    private String shopProdSpecName;

    @ApiModelProperty("单品ID")
    private Long singleProdId;

    @ApiModelProperty("客如云菜品ID")
    private String singleDishId;

    @ApiModelProperty("单品名称")
    private String singleProdName;

    @ApiModelProperty("单品编码")
    private String dishCode;

    @ApiModelProperty("单品单位 ID")
    private String unitId;

    @ApiModelProperty("单品单位名称")
    private String unit;

    @ApiModelProperty("规格加价")
    private BigDecimal addPrice;

    @ApiModelProperty("单品图片")
    private String imageUrl;

    @ApiModelProperty("单品SKU ID")
    private String dishSkuId;
}
