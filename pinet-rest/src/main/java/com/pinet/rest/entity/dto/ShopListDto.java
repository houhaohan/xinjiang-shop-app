package com.pinet.rest.entity.dto;

import com.pinet.core.constants.CommonConstant;
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
    private BigDecimal lat = new BigDecimal(CommonConstant.DEFAULT_LAT);

    @ApiModelProperty(value = "经度")
    private BigDecimal lng = new BigDecimal(CommonConstant.DEFAULT_LNG); //默认西溪银泰店的经纬度
}
