package com.pinet.rest.entity.enums;

import lombok.Getter;

@Getter
public enum CouponShopEnum {

    /**
     * 优惠券店铺
     */
    ALL_SHOP(1,"全部店铺"),
    SOME_SHOP(2,"部分店铺");

    private Integer code;
    private String desc;

    CouponShopEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 通过code取msg
     * @param code 值
     * @return
     */
    public static String getDescByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (CouponShopEnum enums : CouponShopEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums.getDesc();
            }
        }
        return "";
    }
}
