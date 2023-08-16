package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户优惠券
 * </p>
 *
 * @author wlbz
 * @since 2023-08-14
 */
@Getter
@Setter
@TableName("customer_coupon")
@ApiModel(value = "CustomerCoupon对象", description = "用户优惠券")
public class CustomerCoupon extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("优惠券管理id")
    private Long couponGrantId;

    @ApiModelProperty("优惠券发放记录id")
    private Long couponGrantRecordId;

    @ApiModelProperty("用户id")
    private Long customerId;

    @ApiModelProperty("过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDateTime expireTime;

    @ApiModelProperty("优惠券名称")
    private String couponName;

    @ApiModelProperty("优惠券类型  1满减券")
    private Integer couponType;

    @ApiModelProperty("店铺id  0表示所有店可用")
    private Long shopId;

    @ApiModelProperty("店铺名称")
    @TableField(exist = false)
    private String shopName;

    @ApiModelProperty("是否可用")
    @TableField(exist = false)
    private Boolean isUsable;

    @ApiModelProperty("门槛金额")
    private BigDecimal thresholdAmount;

    @ApiModelProperty("优惠券金额")
    private BigDecimal couponAmount;

    @ApiModelProperty("1未领取  2已领取  3已放弃 4已使用")
    private Integer couponStatus;

    @ApiModelProperty("使用规则")
    @TableField(exist = false)
    private String rule;
}
