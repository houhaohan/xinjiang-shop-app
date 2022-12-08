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
 * 商品浏览表
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
@Getter
@Setter
@TableName("product_glance_over")
@ApiModel(value = "ProductGlanceOver对象", description = "商品浏览表")
public class ProductGlanceOver extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品ID")
    private Long prodId;

    @ApiModelProperty("用户ID")
    private Long customerId;

    @ApiModelProperty("浏览次数")
    private Integer times;


}
