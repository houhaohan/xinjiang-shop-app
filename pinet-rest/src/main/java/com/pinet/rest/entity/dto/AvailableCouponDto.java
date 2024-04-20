package com.pinet.rest.entity.dto;

import com.pinet.core.page.PageRequest;
import com.pinet.rest.entity.OrderProduct;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "可用优惠券参数")
public class AvailableCouponDto extends PageRequest {

    @ApiModelProperty("店铺ID")
    private Long shopId;

    @ApiModelProperty("订单商品列表")
    private List<OrderProduct> orderProducts;
}
