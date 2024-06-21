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
 * VIP店铺余额
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
@Getter
@Setter
@TableName("vip_shop_balance")
@ApiModel(value = "VipShopBalance对象", description = "VIP店铺余额")
public class VipShopBalance extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户 ID")
    private Long customerId;

    @ApiModelProperty("店铺ID")
    private Long shopId;

    @ApiModelProperty("可用余额")
    private BigDecimal amount;


}
