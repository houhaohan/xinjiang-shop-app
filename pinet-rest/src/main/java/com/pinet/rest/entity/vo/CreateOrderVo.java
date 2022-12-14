package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: xinjiang-shop-app
 * @description: 创建订单vo
 * @author: hhh
 * @create: 2022-12-14 14:54
 **/
@Data
@ApiModel(value = "CreateOrderVo",description = "创建订单vo")
public class CreateOrderVo {
    @ApiModelProperty(value = "订单id",name = "orderId")
    private Long orderId;

    @ApiModelProperty(value = "订单编号",name = "orderNo")
    private Long orderNo;

    @ApiModelProperty(value = "订单金额",name = "orderPrice")
    private BigDecimal orderPrice;



}
