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
 * 
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@Getter
@Setter
@TableName("shop_product")
@ApiModel(value = "ShopProduct对象", description = "")
public class ShopProduct extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("商品id")
    private Long prodId;

    @ApiModelProperty("店铺商品状态  1正常  2下架")
    private Integer shopProdStatus;


}
