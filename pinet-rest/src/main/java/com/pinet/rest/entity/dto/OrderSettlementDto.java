package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: xinjiang-shop-app
 * @description: 校验订单dto
 * @author: hhh
 * @create: 2022-12-13 14:38
 **/
@Data
@ApiModel(value = "OrderSettlementDto",description = "订单结算dto")
public class OrderSettlementDto {
    @NotNull(message = "店铺id不能为空")
    @ApiModelProperty(value = "店铺id",name = "shopId")
    private Long shopId;

    @NotNull(message = "订单类型不能为空")
    @ApiModelProperty(value = "订单类型( 1外卖  2自提)",name = "orderType")
    private Integer orderType;

    @NotNull(message = "结算方式不能为空")
    @ApiModelProperty(value = "结算类型( 1购物车结算  2直接购买)",name = "settlementType")
    private Integer settlementType;

    /**
     * 店铺商品id
     */
    @ApiModelProperty(value = "店铺商品id(结算类型为直接购买必传)",name = "shopProdId")
    private Long shopProdId;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量(结算类型为直接购买必传)",name = "prodNum")
    private Integer prodNum;

    @ApiModelProperty(value = "商品样式id(结算类型为直接购买必传)",name = "shopProdSpecIds")
    private String shopProdSpecIds;



}
