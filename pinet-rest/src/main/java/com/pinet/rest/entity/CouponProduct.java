package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 优惠券店商品表
 * </p>
 *
 * @author wlbz
 * @since 2024-01-18
 */
@Getter
@Setter
@TableName("coupon_product")
@ApiModel(value = "CouponProduct对象", description = "优惠券店商品表")
public class CouponProduct extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("优惠券ID")
    private Long couponId;

    @ApiModelProperty("店铺商品表ID")
    private Long productId;


}
