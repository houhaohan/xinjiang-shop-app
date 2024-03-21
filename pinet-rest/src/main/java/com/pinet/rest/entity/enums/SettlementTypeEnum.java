package com.pinet.rest.entity.enums;

import lombok.Getter;

/**
 * @description: 订单结算方式枚举
 * @author: chengshuanghui
 * @date: 2024-03-20 12:47
 */
@Getter
public enum SettlementTypeEnum {
    CART_BUY(1,"购物车结算"),
    DIRECT_BUY(2,"直接购买");

    private Integer code;
    private String msg;

    SettlementTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 通过code取枚举
     *
     * @param code 值
     * @return
     */
    public static String getEnumByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (SettlementTypeEnum enums : SettlementTypeEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums.getMsg();
            }
        }
        return "";
    }

}
