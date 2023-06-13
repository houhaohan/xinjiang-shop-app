package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @program: xinjiang-shop-app
 * @description: 订单支付dto
 * @author: hhh
 * @create: 2022-12-19 15:37
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "OrderPayDto",description = "订单支付dto")
public class OrderPayDto extends PayDto{
    @ApiModelProperty(value = "订单id",name = "orderId")
    @NotNull(message = "订单id不能为空")
    private Long orderId;
}
