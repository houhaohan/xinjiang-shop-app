package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderChangeEventDTO {

    @ApiModelProperty("客如云品牌ID")
    private Long brandId;

    @ApiModelProperty("客如云门店ID")
    private Long shopIdenty;

    @ApiModelProperty("订单ID")
    private String orderId;

    @ApiModelProperty("订单状态，WAIT_PROCESSED 待处理, SUCCESS 已完成, WAIT_SETTLED 待结账, SETTLED 已结账, REFUND 已退单, CLOSED 已关闭, INVALID 已作废, CANCELLED 已取消, REJECTED 已拒绝, ANTI_SETTLED 已反结账")
    private String orderStatus;

    @ApiModelProperty("订单类型，FOR_HERE 堂食, PLATFORM_TAKE_OUT 平台外卖, SELF_TAKE_OUT 自营外卖, SELF_TAKE 自提, NO_ORDER_CASHIER 无单收银, MEMBER_STORE 会员充值, MEMBER_MANUAL_STORE 会员补录, REPAYMENT_ORDER 销账订单")
    private String orderType;

    @ApiModelProperty("当前消息ID")
    private String msgId;
}
