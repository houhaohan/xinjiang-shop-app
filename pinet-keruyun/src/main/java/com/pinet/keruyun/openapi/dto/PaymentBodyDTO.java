package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PaymentBodyDTO {
    @ApiModelProperty("支付主单号")
    private String paymentOrderNo;
    @ApiModelProperty("支付明细单号")
    private String payNo;
    @ApiModelProperty("退款主单号")
    private String paymentRefundOrderNo;
    @ApiModelProperty("退款明细单号")
    private String refundNo;
    @ApiModelProperty("支付明细状态")
    private String status;
    @ApiModelProperty("外部支付单号")
    private String outBizId;
    @ApiModelProperty("支付明细外部支付单号")
    private String paymentOrderOutBizId;
}
