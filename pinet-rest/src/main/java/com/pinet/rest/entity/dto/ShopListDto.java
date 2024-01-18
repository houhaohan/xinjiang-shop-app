package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: 店铺列表dto
 * @author: bj
 * @create: 2022/12/12 11:52
 */
@Data
@ApiModel(value = "ShopListDto",description = "店铺列表dto")
public class ShopListDto {
    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

    @ApiModelProperty(value = "经度")
    private BigDecimal lng;

    @ApiModelProperty(value = "城市id")
    private Long cityId;
}
