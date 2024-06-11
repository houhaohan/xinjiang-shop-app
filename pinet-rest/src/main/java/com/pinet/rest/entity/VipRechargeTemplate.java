package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * VIP充值模板
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
@Getter
@Setter
@TableName("vip_recharge_template")
@ApiModel(value = "VipRechargeTemplate对象", description = "VIP充值模板")
public class VipRechargeTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("店铺ID")
    private Long shopId;

    @ApiModelProperty("充值金额")
    private BigDecimal amount;

    @ApiModelProperty("赠送金额")
    private BigDecimal giftAmount;

    @ApiModelProperty("描述")
    private String description;


}
