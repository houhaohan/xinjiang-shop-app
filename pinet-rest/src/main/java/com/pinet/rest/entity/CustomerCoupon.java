package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.pinet.core.entity.BaseEntity;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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

    @ApiModelProperty("优惠券ID")
    private Long couponId;

    @ApiModelProperty("过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expireTime;

    @ApiModelProperty("优惠券名称")
    private String couponName;

    @ApiModelProperty("优惠券类型  1满减券 2-折扣券")
    private Integer couponType;

    @ApiModelProperty("1未领取  2已领取  3已放弃 4已使用")
    private Integer couponStatus;

}
