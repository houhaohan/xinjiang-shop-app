package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class KryScanCodeOrderCreateDTO {

    @ApiModelProperty(value = "部订单号，下单成功后重复会幂等",required = true)
    private String outBizNo;

    @ApiModelProperty("订单备注")
    private String remark;

    @ApiModelProperty("支付明细")
    private List<PaymentDetailRequest> paymentDetailRequestList;

    @ApiModelProperty(value = "订单第二来源， WECHAT_MINI_PROGRAM(“WECHAT_MINI_PROGRAM”, “微信小程序”), ALIPAY_MINI_PROGRAM(“ALIPAY_MINI_PROGRAM”, “支付宝小程序”)",required = true)
    private String orderSecondSource;

    @ApiModelProperty(value = "订单策略，如自动完结。非必填、按需使用",required = true)
    private OrderStrategyRequest orderStrategyRequest;

    @ApiModelProperty(value = "订单优惠金额（单位分）",required = true)
    private Integer promoFee;

    @ApiModelProperty(value = "订单总金额（单位分）",required = true)
    private Integer totalFee;

    @ApiModelProperty(value = "优惠明细入参")
    private List<PromoDetailRequest> promoDetailRequestList;

    @ApiModelProperty("优惠明细入参")
    private DcOrderBizRequest dcOrderBizRequest;

    @ApiModelProperty("附加费费用明细")
    private List<ExtraFeeRequest> extraFeeRequestList;

    @ApiModelProperty(value = "订单应付金额（单位分）",required = true)
    private Integer actualFee;

    @ApiModelProperty("菜品列表入参")
    private List<OrderDishRequest> orderDishRequestList;
}
