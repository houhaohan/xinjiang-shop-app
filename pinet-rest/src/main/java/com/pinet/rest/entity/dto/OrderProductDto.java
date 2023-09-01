package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProductDto {

    @ApiModelProperty("商品主键id")
    private Long id;

    @ApiModelProperty("客如云店铺ID")
    private String kryShopId;

    @ApiModelProperty("商品ID")
    private String prodId;

    @ApiModelProperty("商品编码")
    private String dishCode;

    @ApiModelProperty("单位ID")
    private String unitId;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("菜品类型")
    private String dishType;

    @ApiModelProperty("菜品名称")
    private String productName;

    @ApiModelProperty("菜品图片")
    private String productImg;

    @ApiModelProperty("菜品描述")
    private String productDesc;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("商品单价")
    private BigDecimal prodUnitPrice;

    @ApiModelProperty("商品总价，等于 单价 * 数量")
    private BigDecimal prodPrice;

    @ApiModelProperty("商品数量")
    private Integer prodNum;

    @ApiModelProperty("skuID")
    private String krySkuId;

    @ApiModelProperty("sku编码")
    private String skuCode;

    @ApiModelProperty("sku名称")
    private String skuName;


}
