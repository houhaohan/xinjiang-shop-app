package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @program: xinjiang-shop-app
 * @description: 更新优惠券状态
 * @author: hhh
 * @create: 2023-08-14 13:59
 **/
@Data
public class UpdateCouponStatusDto {
    @ApiModelProperty(value = "用户优惠券id")
    @NotNull(message = "用户优惠券id不能为空")
    private Integer customerCouponId;

    @ApiModelProperty(value = "优惠券状态  2已领取  3已放弃")
    @NotNull(message = "状态不能为空")
    @Min(value = 2,message = "只能更改为领取或放弃")
    @Max(value = 3,message = "只能更改为领取或放弃")
    private Integer couponStatus;
}
