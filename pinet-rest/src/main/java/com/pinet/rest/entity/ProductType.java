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
 * 商品分类表
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@Getter
@Setter
@TableName("product_type")
@ApiModel(value = "ProductType对象", description = "商品分类表")
public class ProductType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品类型名称")
    private String typeName;

    @ApiModelProperty("是否启用 0启用 1禁用")
    private Integer typeState;


}
