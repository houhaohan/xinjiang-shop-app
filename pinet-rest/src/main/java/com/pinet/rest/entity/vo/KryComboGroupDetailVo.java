package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class KryComboGroupDetailVo {

    @ApiModelProperty("套餐明细id")
    private Long id;

    @ApiModelProperty("菜品ID")
    private String dishId;

    @ApiModelProperty("套餐分组ID")
    private Long comboGroupId;

    @ApiModelProperty("子菜菜品ID")
    private String singleDishId;

    @ApiModelProperty("最大可选数量")
    private Integer maxChoose;

    @ApiModelProperty("最小可选数量")
    private Integer minChoose;

    @ApiModelProperty("固定数量")
    private Integer fixChoose;

    @ApiModelProperty("子菜SKU ID")
    private String dishSkuId;

    @ApiModelProperty("套餐分组为可选分组时的子菜加价金额,单位：分")
    private Long dishSkuPrice;

    @ApiModelProperty("可选类型(OPTIONAL-可选/REQUIRED-必选)")
    private String optType;

    @ApiModelProperty("是否为默认子菜")
    private String defaultFlag;

    @ApiModelProperty("子菜名称")
    private String dishName;

    @ApiModelProperty("菜品编码")
    private String dishCode;

    @ApiModelProperty("子菜规格名")
    private String specName;

    @ApiModelProperty("子菜客如云售卖价，单位：分")
    private Long sellPrice;

    @ApiModelProperty("子菜小程序售卖价，单位：分")
    private Long price;

    @ApiModelProperty("子菜是否为多规格 Y:是，N:否")
    private String multiSpecFlag;

    @ApiModelProperty("单位ID")
    private String unitId;

    @ApiModelProperty("单位名称")
    private String unit;

    @ApiModelProperty("商品图片")
    private String imageUrl;

}
