package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel(value = "HotProductVo",description = "热卖商品")
@Data
public class RecommendProductVo {
    @ApiModelProperty(value = "商品ID",notes = "跟prodId值一样")
    private Long id;

    @ApiModelProperty(value = "店铺ID")
    private Long shopId;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "商品ID")
    private Long prodId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品图片")
    private String productImg;

    @ApiModelProperty(value = "商品销售价格")
    private BigDecimal price;

    @ApiModelProperty(value = "商品市场价")
    private BigDecimal marketPrice;


    @ApiModelProperty(value = "商品类型ID")
    private Long productTypeId;

}
