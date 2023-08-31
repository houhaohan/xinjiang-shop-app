package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderBodyDTO {
    @ApiModelProperty("订单产品码(SELF_TAKE_OUT 自外卖；SCAN_CODE_ORDER_PRE 扫码点餐先付;)")
    private String orderProductConfig;
    @ApiModelProperty("订单状态（WAIT_PAY(“WAIT_PAY”, “订单待支付”), PAID(“PAID”, “订单已支付”), SUCCESS(“SUCCESS”, “订单完成”), WAIT_PAY_CLOSE(“WAIT_PAY_CLOSE”, “未支付作废订单”), REFUND(“REFUND”, “订单已退款”)，INVALID(“INVALID”, “订单已失效”), CLOSED(“CLOSED”, “订单已关闭”);）")
    private String status;
    @ApiModelProperty("业务来源（OPEN_PLATFORM(“OPEN_PLATFORM”, “开放平台”)）")
    private String bizSource;
    @ApiModelProperty("履约状态（INIT 初始化;WAIT_RECEIPT 待接单;;RECEIPTED 已接单；REJECTED 已拒单；PACKED 已叫号;SHIPPED 已取餐； DELIVERED 已送达; PUSH_ERROR 推单异常）")
    private String logisticStatus;
    @ApiModelProperty("订单二级来源（WECHAT_MINI_PROGRAM(“WECHAT_MINI_PROGRAM”, “微信小程序”), ALIPAY_MINI_PROGRAM(“ALIPAY_MINI_PROGRAM”, “支付宝小程序”), DOUYIN_MINI_PROGRAM(“DOUYIN_MINI_PROGRAM”, “抖音小程序”), WECHAT_H5(“WECHAT_H5”, “微信H5”), ALIPAY_H5(“ALIPAY_H5”, “支付宝H5”), POS(“POS”, “POS”), MOBILE_POS(“MOBILE_POS”, “移动POS”), CRM_BACKGROUND(“CRM_BACKGROUND”, “CRM后台”), PAD(“PAD”, “PAD下单”), WEB(“WEB”, “PC WEB”);）")
    private String bizSecondSource;
    @ApiModelProperty("二级订单类型 ( DINNER(“DINNER”, “普通正餐”), MIXED(“MIXED”, “混合业态”), FAST_DINNER(“FAST_DINNER”, “普通快餐”), TEA_DRINK(“TEA_DRINK”, “茶饮”))")
    private String orderSecondType;
    @ApiModelProperty("外部交易单号")
    private String outBizId;
    @ApiModelProperty("逆向标记(eventCode= ORDER_SUCCESS且存在该标记时，代表 relatedOrderNo的这个单据 已经退货退款成功)")
    private String antiFlag;
    @ApiModelProperty("外部交易单号")
    private String relatedOrderNo;
    @ApiModelProperty("取餐号")
    private String mealCode;
    @ApiModelProperty("取餐号类（ NUMBER_PLATE(“numberPlate”, “号牌类型”), BUSINESS_SERIAL_NUMBER(“businessSerialNumber”, “流水号”), CUSTOMIZED_PICKUP_CODE(“customizedPickupCode”, “自定义取餐号”), PHONE_NUMBER_PICKUP_CODE(“phoneNumberPickupCode”, “手机尾号取餐号”), WORD_PICKUP_CODE(“wordPickupCode”, “文字口令取餐号”)）")
    private String mealCodeType;
    @ApiModelProperty("退货退款动作")
    private String refundAction;
}
