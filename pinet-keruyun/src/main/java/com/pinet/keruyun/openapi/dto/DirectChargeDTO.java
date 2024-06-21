package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 会员充值参数
 * @author: chengshuanghui
 * @date: 2024-05-09 16:11
 */
@Data
public class DirectChargeDTO {
    @ApiModelProperty("充值时间，格式：yyyy-MM-dd HH:mm:ss")
    private String bizDate;
    @ApiModelProperty("门店id")
    private String shopId;
    @ApiModelProperty("操作人名称（不含特殊字符，长度不超过64位）")
    private String operatorName;
    @ApiModelProperty("用户自定义二级充值渠道")
    private String secondBizChannel;
    @ApiModelProperty("充值金额，单位：分")
    private Long realValue;
    @ApiModelProperty("用户id")
    private String customerId;
    @ApiModelProperty("充值业务id，订单号（不含特殊字符，长度不超过64位）")
    private String bizId;
    @ApiModelProperty("操作人id（数字字符串，长度不超过64位）")
    private String operatorId;
}
