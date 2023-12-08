package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: xinjiang-shop-app
 * @description: 购物车列表vo
 * @author: hhh
 * @create: 2022-12-06 14:44
 **/
@Data
@ApiModel(value = "CartListVo",description = "购物车列表vo")
public class CartListVo {

    @ApiModelProperty(value = "购物车id",name = "cartId")
    private Long cartId;

    @ApiModelProperty(value = "商品id",name = "shopProdId")
    private Long shopProdId;

    @ApiModelProperty(value = "商品名称",name = "prodName")
    private String prodName;

    @ApiModelProperty(value = "商品图片",name = "productImg")
    private String prodImg;

    @ApiModelProperty(value = "商品数量",name = "prodNum")
    private Integer prodNum;

    @ApiModelProperty(value = "商品单价",name = "prodPrice")
    private BigDecimal prodPrice;

    @ApiModelProperty(value = "商品总价(商品单价*商品数量)",name = "allPrice")
    private BigDecimal allPrice;

    @ApiModelProperty(value = "商品样式",name = "prodSpecName")
    private String prodSpecName;

    @ApiModelProperty(value = "购物车状态 1正常  2失效",name = "cartStatus")
    private Integer cartStatus;


}
