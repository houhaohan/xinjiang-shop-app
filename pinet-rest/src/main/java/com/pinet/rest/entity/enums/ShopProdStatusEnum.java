package com.pinet.rest.entity.enums;

import lombok.Getter;

@Getter
public enum ShopProdStatusEnum {


    /**
     * 商品状态
     */
    NORMAL(1,"正常"),
    OFF_SHELF(2,"下架");


    private Integer code;
    private String msg;

    ShopProdStatusEnum() {
    }

    ShopProdStatusEnum(Integer code, String msg) {
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
        for (ShopProdStatusEnum enums : ShopProdStatusEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums.getMsg();
            }
        }

        return "";
    }

}
