package com.pinet.rest.entity;

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
@ApiModel(value = "Cart对象", description = "")
public class Cart extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private Long customerId;

    @ApiModelProperty("商铺id")
    private Long shopId;

    @ApiModelProperty("商品id")
    private Long prodId;

    @ApiModelProperty("商品数量")
    private Integer prodNum;

    @ApiModelProperty("商品sku   id")
    private Long prodSkuId;

    @ApiModelProperty("商品样式id")
    private Long prodSpecId;

    @ApiModelProperty("购物车状态  1正常  2失效")
    private Integer cartStatus;


}
