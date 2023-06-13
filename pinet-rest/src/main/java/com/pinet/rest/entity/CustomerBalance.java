package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 用户余额表
 * </p>
 *
 * @author wlbz
 * @since 2023-06-13
 */
@Getter
@Setter
@TableName("customer_balance")
@ApiModel(value = "CustomerBalance对象", description = "用户余额表")
public class CustomerBalance{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    @TableId(value = "customer_id",type = IdType.INPUT)
    private Long customerId;

    @ApiModelProperty("总余额")
    private BigDecimal balance;

    @ApiModelProperty("可用余额")
    private BigDecimal availableBalance;

    @ApiModelProperty("冻结余额")
    private BigDecimal blockedBalance;


}
