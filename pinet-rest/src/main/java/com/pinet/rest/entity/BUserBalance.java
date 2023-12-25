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
 * 商家资金余额表
 * </p>
 *
 * @author wlbz
 * @since 2023-07-12
 */
@Getter
@Setter
@TableName("b_user_balance")
@ApiModel(value = "BUserBalance对象", description = "商家资金余额表")
public class BUserBalance extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商家ID")
    private Long userId;

    @ApiModelProperty("店铺ID")
    private Long shopId;

    @ApiModelProperty("余额")
    private BigDecimal amount;

    @ApiModelProperty("积分")
    private Integer score;


}
