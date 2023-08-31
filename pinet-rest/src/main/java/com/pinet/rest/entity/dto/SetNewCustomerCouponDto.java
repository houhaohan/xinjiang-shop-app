package com.pinet.rest.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: wlbz-cms
 * @description: 设置新人优惠券
 * @author: hhh
 * @create: 2023-08-17 11:08
 **/
@Data
public class SetNewCustomerCouponDto {

    @ApiModelProperty("优惠券管理id")
    @NotNull(message = "优惠券管理id不能为空")
    private Long couponGrantId;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("过期时间")
    @NotNull(message = "过期时间不能为空")
    private Date expireTime;

    @ApiModelProperty("优惠券名称")
    @NotBlank(message = "优惠券名称不能为空")
    private String couponName;

    @ApiModelProperty("门槛金额")
    @NotNull(message = "门槛金额不能为空")
    private BigDecimal thresholdAmount;

    @ApiModelProperty("优惠金额")
    @NotNull(message = "优惠金额不能为空")
    private BigDecimal couponAmount;

}
