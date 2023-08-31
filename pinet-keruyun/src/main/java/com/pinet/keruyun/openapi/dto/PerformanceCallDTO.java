package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PerformanceCallDTO {

    @ApiModelProperty("客如云订单id")
    private String orderId;

    @ApiModelProperty("云端订单号")
    private String cloudOrderNo;

    @ApiModelProperty("取餐号")
    private String mealCode;

    @ApiModelProperty("叫号次数")
    private String callCount;

    @ApiModelProperty("订单来源 收银POS：KPOS KPOS安卓：KPOS_ANDROID 掌上客如云：K_MOBILE 平板点餐：PAD 扫码点餐：TWO_CODE_SCAN 美团外卖：MEITUAN 饿了么外卖：ELEME 抖音外卖：DOUYIN 开放平台：OPEN_PLATFORM")
    private String orderSource;

    @ApiModelProperty("订单类型 堂食：FOR_HERE 外带：OUT_DINNER 外卖：TAKE_OUT 自营外卖：SELF_TAKEOUT")
    private String orderType;

    @ApiModelProperty("叫号类型 订单叫号：ORDER_CALL 自定义：CUSTOM_CALL")
    private String callType;

    @ApiModelProperty("三方单号")
    private String outBizNo;

    @ApiModelProperty("客侧品牌id")
    private String kryBrandId;

    @ApiModelProperty("域内门店id")
    private String shopIdenty;

    @ApiModelProperty("客侧门店id")
    private String kryShopId;
}
