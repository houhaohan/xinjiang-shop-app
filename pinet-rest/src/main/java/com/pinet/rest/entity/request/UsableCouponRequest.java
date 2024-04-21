package com.pinet.rest.entity.request;

import com.pinet.core.page.PageRequest;
import com.pinet.rest.entity.OrderProduct;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "可用优惠券列表参数")
public class UsableCouponRequest extends PageRequest {

    @ApiModelProperty(value = "店铺 ID",required = true)
    private Long shopId;

    @ApiModelProperty(value = "订单商品",required = true)
    private List<OrderProduct> orderProducts;
}
