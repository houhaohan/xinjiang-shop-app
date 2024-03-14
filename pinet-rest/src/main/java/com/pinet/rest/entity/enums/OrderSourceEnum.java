package com.pinet.rest.entity.enums;

import lombok.Getter;

/**
 * @program:
 * @description: 订单来源enum
 * @author: chengshaunghui
 * @create: 2024-03-08 14:04
 **/
@Getter
public enum OrderSourceEnum {

    WE_CHAT(1,"小程序"),
    APP(2,"APP"),
    SYSTEM(3,"系统生成");


    private Integer code;
    private String msg;

    OrderSourceEnum() {
    }

    OrderSourceEnum(Integer code, String msg) {
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
        for (OrderSourceEnum enums : OrderSourceEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums.getMsg();
            }
        }

        return "";
    }

}
