package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: 会员信息
 * @author: chengshuanghui
 * @date: 2024-06-04 16:58
 */
@Data
public class VipUserVO {
    @ApiModelProperty("用户ID")
    private Long customerId;

    @ApiModelProperty("VIP等级，1-VIP1,2-VIP2,3-VIP3,4-VIP4,5-VIP5")
    private Integer level;

    @ApiModelProperty("VIP名称")
    private String vipName;

    @ApiModelProperty("可用余额")
    private BigDecimal amount;

    @ApiModelProperty("距离下个等级相差金额")
    private BigDecimal nextLevelDiffAmount;
}
