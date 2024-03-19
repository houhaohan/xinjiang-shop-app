package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @description: xinjiang-shop-mini
 * @author: chengshuanghui
 * @date: 2024-03-19 10:12
 */
@Data
public class OrderProductSpecVo {

    @ApiModelProperty("商品sku   id")
    private Long prodSkuId;

    @ApiModelProperty("商品sku名称")
    private String prodSkuName;

    @ApiModelProperty("店铺商品样式id")
    private Long shopProdSpecId;

    @ApiModelProperty("店铺商品样式name")
    private String prodSpecName;

}
