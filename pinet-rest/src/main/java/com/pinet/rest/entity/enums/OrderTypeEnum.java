package com.pinet.rest.entity.enums;

import lombok.Getter;

/**
 * @program:
 * @description: 订单类型枚举
 * @author: chengshaunghui
 * @create: 2024-03-08 14:04
 **/
@Getter
public enum OrderTypeEnum {
    TAKEAWAY(1,"外卖"),
    SELF_PICKUP(2,"自提");


    private Integer code;
    private String msg;

    OrderTypeEnum() {
    }

    OrderTypeEnum(Integer code, String msg) {
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
        for (OrderTypeEnum enums : OrderTypeEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums.getMsg();
            }
        }

        return "";
    }
}
