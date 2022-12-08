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
 * 
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
@Getter
@Setter
@TableName("shop_product_spec")
@ApiModel(value = "ShopProductSpec对象", description = "")
public class ShopProductSpec extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("店铺商品表id")
    private Long shopProdId;

    @ApiModelProperty("商品样式表id")
    private Long prodSpecId;

    @ApiModelProperty("样式名称")
    private String specName;

    @ApiModelProperty("sku id")
    private Long skuId;

    @ApiModelProperty("价格")
    private BigDecimal price;

    @ApiModelProperty("剩余库存")
    private Integer stock;


}
