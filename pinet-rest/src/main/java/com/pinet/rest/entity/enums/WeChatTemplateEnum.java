package com.pinet.rest.entity.enums;

import lombok.Getter;

@Getter
public enum WeChatTemplateEnum {
    COUPON_EXPIRE("1VuNBumcd3eIRf4ZpT5wFN9yiyAh7SZQpF24T3pDIJ8","/pickCodePackage/list/Coupons","优惠券过期提醒"),
    PERFORMANCE_CALL("UQ9ksqAUuRgVLgii5aE3lsfsoO3ciByyED3R9WKZsCA","packageA/orderClose/orderDetails?orderId=","取餐叫号");

    private String key;
    private String pageUrl;
    private String description;

    WeChatTemplateEnum(String key, String pageUrl,String description) {
        this.key = key;
        this.pageUrl = pageUrl;
        this.description = description;
    }

}
