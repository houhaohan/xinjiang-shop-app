package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderSyncDTO {
    @ApiModelProperty("交易主单")
    private String orderNo;
    @ApiModelProperty("订单消息体")
    private OrderBodyDTO orderBody;
    @ApiModelProperty("支付消息体")
    private PaymentBodyDTO paymentBody;
    @ApiModelProperty("消息tag( 订单域:ORDER_WAIT_PAY 订单创建待支付 ORDER_PAID 订单已支付 ORDER_SUCCESS 订单交易完成 ORDER_WAIT_PAY_CLOSE 订单未支付关单 ORDER_REFUND_DISH 订单退菜退款 ORDER_CLOSED 订单预支付后关闭 MODIFY_LOGISTIC_STATUS 修改物流状态 POS_ORDER_CHANGE 订单数据变动)(支付域 PAYMENT_ORDER_PAY 开放交易支付事件 OPEN_TRADE_PAYMENT_REFUND 开放交易退款请求事件 PAYMENT_ORDER_REFUND 开放交易退款事件 )")
    private String eventCode;
    @ApiModelProperty("消息领域（ FULFILL�:履约 PAYMENT�：支付 ORDER: 订单）")
    private String domain;
    @ApiModelProperty("门店id")
    private String shopId;
}
