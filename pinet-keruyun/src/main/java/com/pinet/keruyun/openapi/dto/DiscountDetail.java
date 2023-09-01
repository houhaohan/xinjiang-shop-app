package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ToString
@NoArgsConstructor
public class DiscountDetail {

    @ApiModelProperty(value = "优惠类型", required = true)
    @NotNull
    private Integer discountType;

    @ApiModelProperty(value = "优惠金额", required = true)
    @NotNull
    @Min(value = 0)
    private Integer discountFee;

    @ApiModelProperty(value = "优惠活动或者优惠券id")
    private Long promotionId;

    @ApiModelProperty(value = "优惠券模板id")
    private Long couponId;

    @ApiModelProperty(value = "会员id")
    private Long customerId;

    @ApiModelProperty(value = "优惠描述")
    private String description;

}
