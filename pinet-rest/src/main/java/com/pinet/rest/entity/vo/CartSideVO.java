package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(description = "购物车小料")
public class CartSideVO{
    @ApiModelProperty("购物车小料ID")
    private Long cartSideId;

    @ApiModelProperty("购物车 ID")
    private Long cartId;

    @ApiModelProperty("商品 ID")
    private Long shopProdId;

    @ApiModelProperty("小料明细ID（kry_side_dish_group_detail表主键 ID）")
    private Long sideDetailId;

    @ApiModelProperty("数量")
    private Integer quantity;

    @ApiModelProperty("小料名称")
    private String sideDishName;

    @ApiModelProperty("加价")
    private Long addPrice;
}
