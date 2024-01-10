package com.pinet.rest.entity.dto;

import com.pinet.core.constants.CommonConstant;
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
    @ApiModelProperty(value = "lat",name = "lat")
    private String lat = CommonConstant.DEFAULT_LAT;

    @ApiModelProperty(value = "lng",name = "lng")
    private String lng = CommonConstant.DEFAULT_LAT;

    @NotNull(message = "商品id不能为空")
    @ApiModelProperty(value = "商品id",name = "shopProdId")
    private Long shopProdId;

    private String shopProdName;
}
