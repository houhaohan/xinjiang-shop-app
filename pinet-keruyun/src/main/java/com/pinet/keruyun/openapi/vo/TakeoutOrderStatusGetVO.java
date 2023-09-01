package com.pinet.keruyun.openapi.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TakeoutOrderStatusGetVO {
    @ApiModelProperty("开放平台订单id")
    private String orderId;
    @ApiModelProperty("订单状态,1:未处理,2:已确认,3:已完成(在线付款时: 商家接单就会流转到此状态),4:已取消")
    private Integer status;
    @ApiModelProperty("配送状态,2:待取货,3:配送中,4:已完成,5:已取消")
    private Integer deliveryStatus;
    @ApiModelProperty("订单编号，门店下唯一")
    private String tradeNo;
    @ApiModelProperty("自取状态： 0未取餐， 1已取餐")
    private Integer callDishStatus;
}
