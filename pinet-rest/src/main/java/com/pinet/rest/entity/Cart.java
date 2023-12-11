package com.pinet.rest.entity;

import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 购物车
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
@Getter
@Setter
@ApiModel(value = "Cart对象", description = "购物车")
public class Cart extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private Long customerId;

    @ApiModelProperty("商铺id")
    private Long shopId;

    @ApiModelProperty("菜品ID")
    private String dishId;

    @ApiModelProperty("菜品类型。SINGLE：单菜 ，COMBO：套餐， SIDE：配料")
    private String dishType;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("店铺商品id")
    private Long shopProdId;

    @ApiModelProperty("商品数量")
    private Integer prodNum;

    @ApiModelProperty("购物车状态  1正常  2失效")
    private Integer cartStatus;


}
