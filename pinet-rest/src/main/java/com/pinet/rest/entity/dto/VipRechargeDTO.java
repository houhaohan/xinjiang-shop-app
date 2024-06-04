package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-06-04 13:38
 */
@Data
public class VipRechargeDTO {
    @ApiModelProperty("门店ID")
    @NotNull(message = "请选择充值门店")
    private Long shopId;

    @ApiModelProperty("充值金额")
    @NotNull(message = "充值金额不能为空")
    private BigDecimal amount;

    @ApiModelProperty("赠送的优惠券ID")
    private Long couponId;
}
