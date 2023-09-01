package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PaymentDetailRequest {

    @ApiModelProperty(value = "外部支付明细ID，重复会幂等",required = true)
    private String outBizId;

    @ApiModelProperty(value = "支付金额",required = true)
    private Integer amount;

    @ApiModelProperty(value = "面额,faceAmount=amount+changeAmount")
    private Integer faceAmount;

    @ApiModelProperty(value = "找零金额")
    private Integer changeAmount;

    @ApiModelProperty(value = "支付模式， KEEP_ACCOUNT(“KEEP_ACCOUNT”, “记账”)",required = true)
    private String payMode;

    @ApiModelProperty(value = "支付方式， OPENTRADE_ALIPAY(“支付宝（开放）”, 34, “OPENTRADE_ALIPAY”)," +
            " OPENTRADE_WECHAT_PAY(“微信（开放）”, 35, “OPENTRADE_WECHAT_PAY”), OPENTRADE_OTHER_PAY(“其他（开放）”," +
            " 36, “OPENTRADE_OTHER_PAY”)",required = true)
    private String channelCode;

    @ApiModelProperty(value = "支付类型， PAY_MODE_SMALL_PROGRAM(“PAY_MODE_SMALL_PROGRAM”, “小程序支付”)",required = true)
    private String payType;
}
