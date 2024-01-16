package com.pinet.rest.entity.enums;

import lombok.Getter;

@Getter
public enum DiscountTypeEnum {
    /**
     * 优惠类型
     */
    VIP_1(1,"店帮主"),
    VIP_2(3,"会员"),
    COUPON(2,"优惠券");

    private Integer code;
    private String desc;

    DiscountTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 通过code取D
     * @param code 值
     * @return
     */
    public static DiscountTypeEnum getDescByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (DiscountTypeEnum enums : DiscountTypeEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums;
            }
        }
        return null;
    }
}
