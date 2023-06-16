package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: xinjiang-shop-app
 * @description: 会员中心推荐商品
 * @author: hhh
 * @create: 2023-06-16 10:39
 **/
@Data
public class MemberRecommendProdVo {
    @ApiModelProperty(value = "商品id")
    private Long productId;

    @ApiModelProperty(value = "商品图片")
    private String productImg;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal marketPrice;

    @ApiModelProperty(value = "销量")
    private Integer salesCount;

}
