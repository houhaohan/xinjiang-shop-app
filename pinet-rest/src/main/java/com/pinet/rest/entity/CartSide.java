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
 * 购物车加料
 * </p>
 *
 * @author wlbz
 * @since 2024-04-07
 */
@Getter
@Setter
@TableName("cart_side")
@ApiModel(value = "CartSide对象", description = "购物车加料")
public class CartSide extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("购物车 ID")
    private Long cartId;

    @ApiModelProperty("商品 ID")
    private Long shopProdId;

    @ApiModelProperty("小料明细ID（kry_side_dish_group_detail表主键 ID）")
    private Long sideDetailId;

    @ApiModelProperty("数量")
    private Integer quantity;


}
