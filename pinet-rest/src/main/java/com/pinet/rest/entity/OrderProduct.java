package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单商品表
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
@Getter
@Setter
@TableName("order_product")
@ApiModel(value = "OrderProduct对象", description = "订单商品表")
public class OrderProduct extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("店铺商品id")
    private Long shopProdId;

    @ApiModelProperty("商品名称")
    private String prodName;

    @ApiModelProperty("商品单价")
    private BigDecimal prodUnitPrice;

    @ApiModelProperty("商品数量")
    private Integer prodNum;

    @ApiModelProperty("商品总价(商品单价*商品数量)")
    private BigDecimal prodPrice;

    @ApiModelProperty("商品sku   id")
    private Long prodSkuId;

    @ApiModelProperty("商品sku名称")
    private String prodSkuName;

    @ApiModelProperty("店铺商品样式id")
    private Long shopProdSpecId;

    @ApiModelProperty("店铺商品样式name")
    private String prodSpecName;

    @ApiModelProperty("商品图片")
    private String prodImg;


}
