package com.pinet.rest.entity;

import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@Getter
@Setter
@ApiModel(value = "Product对象", description = "商品表")
public class Product extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("商品图片")
    private String productImg;

    @ApiModelProperty("商品描述(富文本框)")
    private String productDesc;

    @ApiModelProperty("所属分类id")
    private Long productTypeId;

    @ApiModelProperty("所属分类")
    private String productType;


}
