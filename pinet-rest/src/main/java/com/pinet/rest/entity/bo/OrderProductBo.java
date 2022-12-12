package com.pinet.rest.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: xinjiang-shop-app
 * @description: 订单商品bo
 * @author: hhh
 * @create: 2022-12-12 13:51
 **/
@Data
@ApiModel(value = "OrderProductBo",description = "订单列表商品信息")
public class OrderProductBo {
    @ApiModelProperty(value = "订单商品id",name = "orderProductId")
    private Long orderProductId;

    @ApiModelProperty(value = "商品名称",name = "prodName")
    private String prodName;

    @ApiModelProperty(value = "规格",name = "prodSkuName")
    private String prodSkuName;

    @ApiModelProperty(value = "样式",name = "prodSpecName")
    private String prodSpecName;

    @ApiModelProperty(value = "数量",name = "prodNum")
    private Integer prodNum;

    @ApiModelProperty(value = "商品主图",name = "productImg")
    private String productImg;

    @ApiModelProperty(value = "价格",name = "prodPrice")
    private BigDecimal prodPrice;
}
