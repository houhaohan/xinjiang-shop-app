package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "ShopProductVo",description = "商品详情")
public class ShopProductVo {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "商品ID")
    private Long prodId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品图片")
    private String productImg;

    @ApiModelProperty(value = "商品描述，富文本")
    private String productDesc;

    @ApiModelProperty(value = "商品分类ID")
    private Long productTypeId;

    @ApiModelProperty(value = "商品分类名称")
    private String productType;

    @ApiModelProperty(value = "商品售价")
    private BigDecimal price;

    @ApiModelProperty(value = "商品市场价格")
    private BigDecimal marketPrice;

    @ApiModelProperty(value = "店铺商品样式id")
    private Integer shopProdSpecId;

    @ApiModelProperty(value = "sku")
    private List<ShopProductSkuVo> skuList = new ArrayList<>();

}
