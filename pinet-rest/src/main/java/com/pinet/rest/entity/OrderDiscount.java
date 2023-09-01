package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pinet.core.entity.BaseEntity;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
 * <p>
 * 订单优惠明细表
 * </p>
 *
 * @author wlbz
 * @since 2023-08-22
 */
@Getter
@Setter
@TableName("order_discount")
@ApiModel(value = "OrderDiscount对象", description = "订单优惠明细表")
@Accessors(chain=true)
public class OrderDiscount extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("优惠金额")
    private BigDecimal discountAmount;

    @ApiModelProperty("优惠描述")
    private String discountMsg;

    @ApiModelProperty("优惠类型  1店帮主  2优惠券")
    @TableField("`type`")
    private Integer type;


}
