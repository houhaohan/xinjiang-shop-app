package com.pinet.rest.entity.enums;

import lombok.Getter;

/**
 * @description: 支付渠道的枚举
 * @author: chengshuanghui
 * @date: 2024-06-18 14:30
 */
@Getter
public enum  OrderPayChannelEnum {
    WE_CHAT("weixin_mini","微信小程序支付"),
    BALANCE("balance","余额支付");

    private String channelId;
    private String description;

    OrderPayChannelEnum() {
    }

    OrderPayChannelEnum(String channelId, String description) {
        this.channelId = channelId;
        this.description = description;
    }
}
