package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商品样式表
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@Getter
@Setter
@TableName("product_spec")
@ApiModel(value = "ProductSpec对象", description = "商品样式表")
public class ProductSpec extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品样式名称")
    private String specName;

    @ApiModelProperty("商品规格id")
    private Long skuId;

    @ApiModelProperty("商品id")
    private Long productId;


}
