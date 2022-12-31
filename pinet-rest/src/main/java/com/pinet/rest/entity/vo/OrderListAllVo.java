package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: xinjiang-shop-app
 * @description: app订单列表vo
 * @author: hhh
 * @create: 2022-12-31 09:43
 **/
@Data
@ApiModel(value = "OrderListAllVo",description = "app订单列表vo")
public class OrderListAllVo {

    @ApiModelProperty(value = "订单id",name = "orderId")
    private Long orderId;

    @ApiModelProperty(value = "订单编号",name = "orderNo")
    private Long orderNo;

    @ApiModelProperty(value = "订单类型(1仓超订单  2轻食订单)",name = "orderType")
    private Integer orderType;

    @ApiModelProperty(value = "店铺id",name = "shopId")
    private Long shopId;

    @ApiModelProperty(value = "店铺名称",name = "shopName")
    private String shopName;

    @ApiModelProperty(value = "订单状态",name = "orderStatus")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单状态str",name = "orderStatusStr")
    private String orderStatusStr;






}
