package com.pinet.rest.entity.dto;

import com.pinet.rest.entity.common.CommonPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: bj
 * @create: 2022/12/12 17:07
 */
@Data
public class ProductTypeDto {
    @ApiModelProperty(value = "店铺ID")
    private Long shopId;

    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

    @ApiModelProperty(value = "经度")
    private BigDecimal lng;


}
