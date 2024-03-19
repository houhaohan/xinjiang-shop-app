package com.pinet.rest.entity.enums;

import lombok.Getter;

@Getter
public enum CartStatusEnum {

    /**
     * 购物车状态  1正常  2失效
     */
    NORMAL(1,"正常"),
    EXPIRE(2,"失效");

    private Integer code;

    private String msg;

    CartStatusEnum() {
    }

    CartStatusEnum(Integer code, String msg) {
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
        for (CartStatusEnum enums : CartStatusEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums.getMsg();
            }
        }

        return "";
    }



}
