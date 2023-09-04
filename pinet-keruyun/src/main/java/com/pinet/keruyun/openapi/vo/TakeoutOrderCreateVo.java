package com.pinet.keruyun.openapi.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TakeoutOrderCreateVo {

    @ApiModelProperty("信息说明")
    private String formatMsgInfo;

    @ApiModelProperty("信息码")
    private String msgCode;

    @ApiModelProperty("信息说明")
    private String msgInfo;

    @ApiModelProperty("是否成功")
    private String success;

    @ApiModelProperty("服务编码")
    private Integer serverCode;

    @ApiModelProperty("服务消息ID")
    private String messageId;

    @ApiModelProperty("响应数据体")
    private ScanCodePrePlaceOrderVo.OrderInfo data;

    @Data
    public static class OrderInfo{
        @ApiModelProperty("订单号")
        private String orderNo;

        @ApiModelProperty("订单业务时间，即订单创建时间")
        private Long orderBizTime;

        @ApiModelProperty("业务流水号，是一个递增序列号")
        private String bizSerialNum;

        @ApiModelProperty("支付更新时间")
        private Long paymentUpdateTime;

        @ApiModelProperty("支付总单状态")
        private String payStatementStatus;
    }
}
