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
 * 
 * </p>
 *
 * @author wlbz
 * @since 2023-08-14
 */
@Getter
@Setter
@TableName("coupon_grant")
@ApiModel(value = "CouponGrant对象", description = "")
public class CouponGrant extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("发放对象")
    private String subject;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("bean name")
    private String serviceName;

    @ApiModelProperty("1运营端  2商家端")
    private Integer type;


}
