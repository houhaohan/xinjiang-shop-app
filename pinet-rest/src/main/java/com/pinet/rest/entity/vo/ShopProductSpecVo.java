package com.pinet.rest.entity.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;

/**
 * <p>
 *  商品样式
 * </p>
 *
 * @author chengshuanghui
 * @since 2022-12-09
 */
@Data
@ApiModel(value = "ShopProductSpecVo对象", description = "sku 样式")
public class ShopProductSpecVo{

    @ApiModelProperty("规格Id")
    private Long prodSpecId;

    @ApiModelProperty("规格名称")
    private String specName;

    @ApiModelProperty("sku id")
    private Long skuId;

    @ApiModelProperty("售价")
    private BigDecimal price;

    @ApiModelProperty("市场价")
    private BigDecimal marketPrice;

}
