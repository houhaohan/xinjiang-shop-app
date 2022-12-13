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

}
