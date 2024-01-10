package com.pinet.rest.entity.enums;

import lombok.Getter;

@Getter
public enum CouponTypeEnum {
    /**
     * 优惠券类型
     */
    FULL_REDUC(1,"满减优惠券"),
    DISCOUNT(2,"折扣优惠券");

    private Integer code;
    private String desc;

    CouponTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 通过code取D
     * @param code 值
     * @return
     */
    public static CouponTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (CouponTypeEnum enums : CouponTypeEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums;
            }
        }
        return null;
    }


    /**
     * 通过code取D
     * @param code 值
     * @return
     */
    public static String getDescByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (CouponTypeEnum enums : CouponTypeEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums.getDesc();
            }
        }
        return null;
    }
}
