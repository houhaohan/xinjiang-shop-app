package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-04-09 11:04
 */
@Data
public class OrderSideVo {
    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单商品 ID")
    private Long orderProdId;

    @ApiModelProperty("小料明细ID（kry_side_dish_group_detail表主键 ID）")
    private Long sideDetailId;

    @ApiModelProperty("小料ID")
    private String sideDishId;

    @ApiModelProperty("小料名称")
    private String sideDishName;

    @ApiModelProperty("数量")
    private Integer quantity;

    @ApiModelProperty("加价(单价)")
    private BigDecimal addPrice;

    @ApiModelProperty("总价（单价*数量）")
    private BigDecimal totalPrice;
}
