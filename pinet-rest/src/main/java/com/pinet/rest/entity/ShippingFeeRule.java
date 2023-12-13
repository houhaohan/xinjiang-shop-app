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
 * 配送费规则
 * </p>
 *
 * @author wlbz
 * @since 2023-12-13
 */
@Getter
@Setter
@TableName("shipping_fee_rule")
@ApiModel(value = "ShippingFeeRule对象", description = "配送费规则")
public class ShippingFeeRule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("开始距离")
    private Integer startDistance;

    @ApiModelProperty("结束距离")
    private Integer endDistance;

    @ApiModelProperty("配送费")
    private BigDecimal shippingFee;


}
