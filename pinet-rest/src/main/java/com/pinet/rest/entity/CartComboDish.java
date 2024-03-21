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
 * 购物车套餐菜品表
 * </p>
 *
 * @author wlbz
 * @since 2024-03-15
 */
@Getter
@Setter
@TableName("cart_combo_dish")
@ApiModel(value = "CartComboDish对象", description = "购物车套餐菜品表")
public class CartComboDish extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("购物车id")
    private Long cartId;

    @ApiModelProperty("套餐内菜品id")
    private Long shopProdId;


}
