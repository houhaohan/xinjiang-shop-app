package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @program: xinjiang-shop-app
 * @description: 创建订单dto
 * @author: hhh
 * @create: 2022-12-14 15:01
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "CreateOrderDto",description = "创建订单dto")
public class CreateOrderDto extends OrderSettlementDto{
    @ApiModelProperty(value = "收货地址id(外卖订单必传)",name = "customerAddressId")
    private Long customerAddressId;

    /**
     * 该参数不做具体逻辑处理  只用来对比结算和创建订单金额是否相同
     */
    @NotNull(message = "订单总金额(订单结算接口返回的)")
    @ApiModelProperty(value = "订单总金额",name = "orderPrice")
    private BigDecimal orderPrice;

    @NotBlank(message = "lat不能为空")
    @ApiModelProperty(value = "lat",name = "lat")
    private String lat;

    @NotBlank(message = "lng不能为空")
    @ApiModelProperty(value = "lng",name = "lng")
    private String lng;

    @ApiModelProperty(value = "备注",name = "remark")
    private String remark;

}
