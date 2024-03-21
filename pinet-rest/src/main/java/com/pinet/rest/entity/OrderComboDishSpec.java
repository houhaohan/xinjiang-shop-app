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
 * 订单套餐菜品样式表
 * </p>
 *
 * @author wlbz
 * @since 2024-03-20
 */
@Getter
@Setter
@TableName("order_combo_dish_spec")
@ApiModel(value = "OrderComboDishSpec对象", description = "订单套餐菜品样式表")
public class OrderComboDishSpec extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单套餐菜品表主键ID")
    private Long orderComboDishId;

    @ApiModelProperty("样式id")
    private Long shopProdSpecId;

    @ApiModelProperty("样式名称")
    private String shopProdSpecName;

    @ApiModelProperty("加价")
    private BigDecimal addPrice;


}
