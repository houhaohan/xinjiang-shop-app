package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @program: xinjiang-shop-app
 * @description: 购物车商品数量修改
 * @author: hhh
 * @create: 2022-12-08 13:36
 **/
@Data
public class EditCartProdNumDto {
    @NotNull(message = "购物车id不能为空")
    @ApiModelProperty(value = "购物车id",name = "cartId")
    private Integer cartId;

    @NotNull(message = "商品数量不能为空")
    @Min(value = 0,message = "商品数量最小为0")
    @ApiModelProperty(value = "商品数量(数量为0时则会删除该记录)",name = "prodNum")
    private Integer prodNum;
}
