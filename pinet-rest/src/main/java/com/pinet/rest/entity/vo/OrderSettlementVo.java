package com.pinet.rest.entity.vo;

import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.bo.OrderProductBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: xinjiang-shop-app
 * @description: 订单结算vo
 * @author: hhh
 * @create: 2022-12-13 15:02
 **/
@Data
@ApiModel(value = "OrderSettlementVo",description = "订单结算vo")
public class OrderSettlementVo {
    @ApiModelProperty(value = "总金额",name = "orderPrice")
    private BigDecimal orderPrice;

    @ApiModelProperty(value = "配送费",name = "shippingFee")
    private BigDecimal shippingFee;

    @ApiModelProperty(value = "制作中数量",name = "orderMakeCount")
    private Integer orderMakeCount;

    @ApiModelProperty(value = "订单商品信息",name = "orderProductBoList")
    private List<OrderProduct> orderProductBoList;

    @ApiModelProperty(value = "预计送达时间",name = "estimateArrivalTime")
    private String estimateArrivalTime;
}
