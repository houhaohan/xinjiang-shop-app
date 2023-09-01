package com.pinet.keruyun.openapi.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PromoDetailRequest {

    @ApiModelProperty("外部的优惠唯一标识行ID，无业务含义")
    private String outPromoDetailId;

    @ApiModelProperty("优惠标识ID（券ID/活动ID），积分时可为空；券和活动不能为空")
    private String promoId;

    @ApiModelProperty("优惠名称")
    private String promoName;

    @ApiModelProperty("优惠金额")
    private Long promoFee;

    @ApiModelProperty("扣，例如5折该字段为500")
    private Integer promoDiscount;

    @ApiModelProperty("优惠类型， THIRD_MERCHANT(“THIRD_MERCHANT”, “三方商家优惠")
    private String promoType;

    @ApiModelProperty("优惠维度， TOATL_CART(“TOATL_CART”, “整单维度”")
    private String promoDimension;

    @ApiModelProperty("优惠类别， /* 订单级优惠，影响订单应付金额 / ORDER_DIMENSION(“ORDER_DIMENSION”, “订单级优惠”), / 支付级优惠，不影响订单应付金额，影响订单实收")
    private String promoCategory;
}
