package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * VIP等级折扣
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
@Getter
@Setter
@TableName("vip_discount")
@ApiModel(value = "VipDiscount对象", description = "VIP等级折扣")
public class VipDiscount extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("VIP等级ID")
    private Long levelId;

    @ApiModelProperty("VIP等级")
    @TableField("`level`")
    private Long level;

    @ApiModelProperty("折扣，例：95折就存95")
    private Integer discount;

}
