package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单套餐菜品表
 * </p>
 *
 * @author wlbz
 * @since 2024-03-20
 */
@Getter
@Setter
@TableName("order_combo_dish")
@ApiModel(value = "OrderComboDish对象", description = "订单套餐菜品表")
public class OrderComboDish extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("套餐id")
    private Long shopProdId;

    @ApiModelProperty("单品id")
    private Long singleProdId;

    @ApiModelProperty("单品客如云菜品id")
    private String dishId;

    @ApiModelProperty("菜品名称")
    private String prodName;

    @ApiModelProperty("单位ID")
    private String unitId;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("商品数量")
    private Integer quantity;

    @ApiModelProperty("图片")
    private String imageUrl;


}
