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
 * 购物车套餐菜品样式表
 * </p>
 *
 * @author wlbz
 * @since 2024-03-15
 */
@Getter
@Setter
@TableName("cart_combo_dish_spec")
@ApiModel(value = "CartComboDishSpec对象", description = "购物车套餐菜品样式表")
public class CartComboDishSpec extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("购物车id")
    private Long cartId;

    @ApiModelProperty("套餐菜品id")
    private Long shopProdId;

    @ApiModelProperty("样式id")
    private Long shopProdSpecId;

    @ApiModelProperty("样式名称")
    private String shopProdSpecName;


}
