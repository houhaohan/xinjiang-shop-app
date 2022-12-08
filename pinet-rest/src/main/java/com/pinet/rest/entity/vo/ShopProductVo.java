package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ShopProductVo",description = "商品详情")
public class ShopProductVo {

    @ApiModelProperty(value = "商品ID")
    private Long prodId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品图片")
    private String productImg;

    @ApiModelProperty(value = "商品描述，富文本")
    private String productDesc;

    @ApiModelProperty(value = "店铺ID")
    private Long shopId;

    @ApiModelProperty(value = "商品分类ID")
    private Long productTypeId;

    @ApiModelProperty(value = "商品分类名称")
    private Long productType;

}
