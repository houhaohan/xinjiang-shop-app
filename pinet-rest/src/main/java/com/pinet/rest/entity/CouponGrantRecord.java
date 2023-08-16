package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 优惠券发放记录
 * </p>
 *
 * @author wlbz
 * @since 2023-08-14
 */
@Getter
@Setter
@TableName("coupon_grant_record")
@ApiModel(value = "CouponGrantRecord对象", description = "优惠券发放记录")
public class CouponGrantRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("店铺id  0为运营端发券")
    private Long shopId;

    @ApiModelProperty("发券管理id")
    private Long couponGrantId;

    @ApiModelProperty("发放主体")
    private String subject;

    @ApiModelProperty("发放人数")
    private Integer num;

    @ApiModelProperty("发放时间")
    private LocalDateTime grantTime;

    @ApiModelProperty("备注")
    private String remark;


}
