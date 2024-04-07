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
 * 订单加料
 * </p>
 *
 * @author wlbz
 * @since 2024-04-08
 */
@Getter
@Setter
@TableName("order_side")
@ApiModel(value = "OrderSide对象", description = "订单加料")
public class OrderSide extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单商品 ID")
    private Long orderProdId;

    @ApiModelProperty("小料明细ID（kry_side_dish_group_detail表主键 ID）")
    private Long sideDetailId;

    @ApiModelProperty("数量")
    private Integer quantity;

    @ApiModelProperty("加价(单价)")
    private BigDecimal addPrice;

    @ApiModelProperty("总价（单价*数量）")
    private BigDecimal totalPrice;


}
