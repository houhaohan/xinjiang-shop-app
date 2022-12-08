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
 * 店铺商品表
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
@Getter
@Setter
@TableName("shop_product")
@ApiModel(value = "ShopProduct对象", description = "店铺商品表")
public class ShopProduct extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("商品id")
    private Long prodId;

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

    @ApiModelProperty("店铺商品状态  1正常  2下架")
    private Integer shopProdStatus;


}
