package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class KryOpenTakeoutOrderCreateDTO {

    @ApiModelProperty(value = "外部订单号，下单成功后重复会幂等",required = true)
    private String outBizNo;

    @ApiModelProperty("订单备注")
    private String remark;

    @ApiModelProperty("支付明细")
    private List<PaymentDetailRequest> paymentDetailRequestList;

    @ApiModelProperty("订单第二来源， /** 微信小程序 WECHAT_MINI_PROGRAM,  支付宝小程序 ALIPAY_MINI_PROGRAM")
    private String orderSecondSource;

    @ApiModelProperty("订单策略，如自动完结。非必填，按需使用")
    private OrderStrategyRequest orderStrategyRequest;

    @ApiModelProperty(value = "订单优惠金额（单位分）",required = true)
    private Integer promoFee;

    @ApiModelProperty(value = "订单总金额（单位分）",required = true)
    private Integer totalFee;

    @ApiModelProperty("订单附加费列表")
    private List<PromoDetailRequest> promoDetailRequestList;

    @ApiModelProperty("点餐单业务参数")
    private DcOrderBizRequest dcOrderBizRequest;

    @ApiModelProperty("订单附加费列表")
    private List<ExtraFeeRequest> extraFeeRequestList;

    @ApiModelProperty("订单应付金额（单位分）")
    private Long actualFee;

    @ApiModelProperty("菜品模型")
    private List<OrderDishRequest> orderDishRequestList;

    @ApiModelProperty("订单配送相关信息")
    private List<DeliveryInfoRequest> deliveryInfoRequestList;
}
