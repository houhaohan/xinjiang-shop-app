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
 * 商品规格表
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@Getter
@Setter
@TableName("product_sku")
@ApiModel(value = "ProductSku对象", description = "商品规格表")
public class ProductSku extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("规格名称")
    private String skuName;


}
