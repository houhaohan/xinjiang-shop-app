package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @program: xinjiang-shop-app
 * @description: 订单支付dto
 * @author: hhh
 * @create: 2022-12-19 15:37
 **/
@Data
@ApiModel(value = "OrderPayDto",description = "订单支付dto")
public class OrderPayDto {
    @ApiModelProperty(value = "渠道id(alipay_app,weixin_app，weixin_mini,balance)",name = "channelId")
    @NotBlank(message = "渠道id不能为空")
    private String channelId;

    @ApiModelProperty(value = "订单id",name = "orderId")
    @NotNull(message = "订单id不能为空")
    private Long orderId;

    @ApiModelProperty(value = "订单金额(支付金额)",name = "orderPrice")
    @NotBlank(message = "订单支付金额不能为空")
    private BigDecimal orderPrice;

    @ApiModelProperty(value = "openId(小程序支付必传)",name = "openId")
    private String openId;

    @ApiModelProperty(value = "支付密码(余额支付必传)",name = "payPassword")
    private String payPassword;
}
