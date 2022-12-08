package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @program: xinjiang-shop-app
 * @description: 添加购物车dto
 * @author: hhh
 * @create: 2022-12-08 11:42
 **/
@Data
public class AddCartDto {
    @NotNull(message = "店铺id不能为空")
    @ApiModelProperty(value = "店铺id",name = "shopId")
    private Integer shopId;

    @NotNull(message = "店铺商品id不能为空")
    @ApiModelProperty(value = "店铺商品id",name = "shopProdId")
    private Integer shopProdId;

    @NotNull(message = "数量不能为空")
    @Min(value = 0,message = "数量最小为0")
    @ApiModelProperty(value = "商品数量",name = "prodNum")
    private Integer prodNum;

    @NotNull(message = "店铺商品样式id不能为空")
    @ApiModelProperty(value = "商品样式id",name = "shopProdSpecId")
    private Integer shopProdSpecId;


}
