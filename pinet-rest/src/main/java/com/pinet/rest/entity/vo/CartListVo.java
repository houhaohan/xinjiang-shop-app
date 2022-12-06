package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    private Integer cartId;

    @ApiModelProperty(value = "商品名称",name = "prodName")
    private String prodName;

    @ApiModelProperty(value = "商品图片",name = "productImg")
    private String prodImg;

    @ApiModelProperty(value = "商品数量",name = "prodNum")
    private Integer prodNum;

    @ApiModelProperty(value = "商品规格",name = "prodSkuName")
    private String prodSkuName;

    @ApiModelProperty(value = "商品样式",name = "prodSpecId")
    private String prodSpecId;

    @ApiModelProperty(value = "购物车状态 1正常  2失效",name = "cartStatus")
    private Integer cartStatus;


}
