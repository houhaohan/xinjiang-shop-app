package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-06-04 16:37
 */
@Data
public class VipRechargeTemplateVO {
    @ApiModelProperty("店铺ID")
    private Long shopId;

    @ApiModelProperty("充值金额")
    private BigDecimal amount;

    @ApiModelProperty("赠送金额")
    private BigDecimal giftAmount;

    @ApiModelProperty("总金额 = 充值金额 + 赠送金额 ")
    private BigDecimal totalAmount;

    @ApiModelProperty("描述 ")
    private String description;

    @ApiModelProperty("是否推荐，0-否，1-是 ")
    private Integer recommend;
}
