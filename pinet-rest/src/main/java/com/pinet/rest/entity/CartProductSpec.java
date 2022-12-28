package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 购物车商品样式表
 * </p>
 *
 * @author wlbz
 * @since 2022-12-28
 */
@Getter
@Setter
@TableName("cart_product_spec")
@ApiModel(value = "CartProductSpec对象", description = "购物车商品样式表")
public class CartProductSpec extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("购物车id")
    private Long cartId;

    @ApiModelProperty("店铺商品样式id")
    private Long shopProdSpecId;

    @ApiModelProperty("店铺商品样式名称")
    private String shopProdSpecName;

    @TableField(exist = false)
    private BigDecimal price;


}
