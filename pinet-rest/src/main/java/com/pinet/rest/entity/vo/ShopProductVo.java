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

    @ApiModelProperty(value = "菜品类型。SINGLE：单菜 ，COMBO：套餐， SIDE：配料")
    private String dishType;

    @ApiModelProperty(value = "商品售价")
    private BigDecimal price = BigDecimal.ZERO;

    @ApiModelProperty(value = "商品市场价格")
    private BigDecimal marketPrice = BigDecimal.ZERO;

    @ApiModelProperty(value = "店铺ID")
    private Long shopId;

    @ApiModelProperty(value = "sku")
    private List<ShopProductSkuVo> skuList = new ArrayList<>();

    @ApiModelProperty(value = "标签")
    private String labels;

    @ApiModelProperty(value = "销量")
    private Long saleCount;


}
