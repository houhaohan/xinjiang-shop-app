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
 * @since 2022-12-29
 */
@Getter
@Setter
@TableName("order_product_spec")
@ApiModel(value = "OrderProductSpec对象", description = "")
public class OrderProductSpec extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("订单商品id")
    private Long orderProdId;

    @ApiModelProperty("商品sku   id")
    private Long prodSkuId;

    @ApiModelProperty("商品sku名称")
    private String prodSkuName;

    @ApiModelProperty("店铺商品样式id")
    private Long shopProdSpecId;

    @ApiModelProperty("店铺商品样式name")
    private String prodSpecName;


}
