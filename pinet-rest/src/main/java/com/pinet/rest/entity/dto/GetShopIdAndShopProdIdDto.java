package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: xinjiang-shop-app
 * @description:
 * @author: hhh
 * @create: 2023-06-16 14:35
 **/
@Data
public class GetShopIdAndShopProdIdDto {
    @NotBlank(message = "经纬度不能为空")
    @ApiModelProperty(value = "lat",name = "lat")
    private String lat;

    @NotBlank
    @NotBlank(message = "经纬度不能为空")
    @ApiModelProperty(value = "lng",name = "lng")
    private String lng;

    @NotNull(message = "商品id不能为空")
    @ApiModelProperty(value = "商品id",name = "productId")
    private Long productId;
}
