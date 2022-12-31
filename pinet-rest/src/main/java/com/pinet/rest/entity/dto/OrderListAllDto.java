package com.pinet.rest.entity.dto;

import com.pinet.rest.entity.common.CommonPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: xinjiang-shop-app
 * @description: app订单列表dto
 * @author: hhh
 * @create: 2022-12-30 15:27
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "OrderListAllDto",description = "app订单列表dto")
public class OrderListAllDto extends CommonPage {
    @ApiModelProperty(value = "订单类型 0全部 1代付款 2待收货 3自提 4待评价 5交易完成",name = "orderType")
    private Integer orderType;
}
