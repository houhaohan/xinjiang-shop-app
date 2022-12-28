package com.pinet.rest.entity.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("店铺商品搜索")
public class ShopProductParam {

    @ApiModelProperty(value = "店铺ID",required = true)
    private Long shopId;

    @ApiModelProperty(value = "商品名称",required = true)
    private String productName;

    @ApiModelProperty("纬度")
    private BigDecimal lat;

    @ApiModelProperty("经度")
    private BigDecimal lng;
}
