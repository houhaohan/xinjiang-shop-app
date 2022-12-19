package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: xinjiang-shop-app
 * @description: 订单支付vo
 * @author: hhh
 * @create: 2022-12-19 15:51
 **/
@Data
@ApiModel(value = "OrderPayVo",description = "订单支付vo")
public class OrderPayVo {
    @ApiModelProperty(value = "appId",name = "appId")
    private String appId;

    @ApiModelProperty(value = "时间戳",name = "timeStamp")
    private String timeStamp;

    @ApiModelProperty(value = "随机字符串",name = "nonceStr")
    private String nonceStr;

    @ApiModelProperty(value = "订单详情扩展字符串",name = "packageStr")
    private String packageStr;

    @ApiModelProperty(value = "签名方式",name = "signType")
    private String signType;

    @ApiModelProperty(value = "签名",name = "paySign")
    private String paySign;
}
