package com.pinet.rest.entity.vo;

import com.pinet.core.constants.CommonConstant;
import com.pinet.rest.entity.OrderDiscount;
import com.pinet.rest.entity.OrderProduct;
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
@ApiModel(value = "OrderSettlementVo", description = "订单结算vo")
public class OrderSettlementVo {
    @ApiModelProperty(value = "总金额(实付金额)", name = "orderPrice")
    private BigDecimal orderPrice;

    @ApiModelProperty(value = "订单总价(折扣前金额)", name = "originalPrice")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "商品总价(折扣前金额)", name = "originalOrderProductPrice")
    private BigDecimal originalOrderProductPrice;

    @ApiModelProperty(value = "配送费", name = "shippingFee")
    private BigDecimal shippingFee;

    @ApiModelProperty(value = "打包费", name = "packageFee")
    private BigDecimal packageFee;

    @ApiModelProperty(value = "制作中数量", name = "orderMakeCount")
    private Integer orderMakeCount;

    @ApiModelProperty(value = "最大制作数量", name = "orderMaxNum")
    private Integer orderMaxNum = CommonConstant.MAX_ORDER_NUM;

    @ApiModelProperty(value = "订单商品信息", name = "orderProductBoList")
    private List<OrderProductVo> orderProductBoList;

    @ApiModelProperty(value = "订单商品数量", name = "orderProductNum")
    private Integer orderProductNum;

    @ApiModelProperty(value = "预计送达时间", name = "estimateArrivalTime")
    private String estimateArrivalTime;

    @ApiModelProperty(value = "店铺名称", name = "shopName")
    private String shopName;

    @ApiModelProperty(value = "订单优惠明细", name = "orderDiscounts")
    private List<OrderDiscount> orderDiscounts;

    private List<CustomerCouponVo> customerCoupons;
}
