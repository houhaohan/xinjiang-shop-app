package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * VIP等级表
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
@Getter
@Setter
@TableName("vip_level")
@ApiModel(value = "VipLevel对象", description = "VIP等级表")
public class VipLevel extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("VIP等级，1-VIP1,2-VIP2,3-VIP3,4-VIP4,5-VIP5")
    @TableField("`level`")
    private Integer level;

    @ApiModelProperty("VIP名称")
    private String vipName;

    @ApiModelProperty("门槛金额")
    private BigDecimal minAmount;

}
