package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeliveryInfoRequest {
    @ApiModelProperty("交付信息枚举（RECIPIENT_ADDRESS(“RECIPIENT_ADDRESS”, “订单交付地址”)）")
    private String deliveryType;

    @ApiModelProperty("外部订单号")
    private String outBizId;

    @ApiModelProperty("期望送达时间")
    private Long expectDeliveryTime;

    @ApiModelProperty("收货人姓名")
    private String receiverName;

    @ApiModelProperty("收货人地址")
    private String receiverAddress;

    @ApiModelProperty("MAN/WOMAN")
    private String receiverGender;

    @ApiModelProperty("收货人联系方式")
    private String receiverPrimaryPhone;

    @ApiModelProperty("收货人备用联系方式")
    private String receiverSecondPhone;

    @ApiModelProperty("地图类型（BAIDU(1, “百度地图”), GD(2, “高德地图”)）")
    private Integer mapType;

    @ApiModelProperty("收货地址经度")
    private BigDecimal longitude;

    @ApiModelProperty("收货地址纬度")
    private BigDecimal latitude;
}
