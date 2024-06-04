package com.pinet.rest.entity.enums;

import lombok.Getter;

/**
 * @program: xinjiang-shop-zyt
 * @description: 支付记录类型枚举
 * @author: hhh
 * @create: 2023-04-17 09:59
 **/
@Getter
public enum PayTypeEnum {
    /**
     * 支付类型
     */
    ORDER_PAY(1,"轻食订单支付","order_pay_notify_service"),
    DBZ_RECHARGE(2,"店帮主充值","recharge_notify_service"),
    VIP_RECHARGE(3,"会员充值","vip_recharge_notify_service");



    private Integer code;
    private String msg;
    private String service;


    PayTypeEnum(Integer code, String msg, String service) {
        this.code = code;
        this.msg = msg;
        this.service = service;
    }


    /**
     * 通过code取枚举
     *
     * @param code 值
     * @return
     */
    public static PayTypeEnum getEnumByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PayTypeEnum enums : PayTypeEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums;
            }
        }

        return null;
    }
}
