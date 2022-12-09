package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("sku")
public class ShopProductSkuVo {

    @ApiModelProperty("skuId")
    private Long skuId;

    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("sku规格")
    private List<ShopProductSpecVo> skuSpecs = new ArrayList<>();
}
