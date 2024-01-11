package com.pinet.rest.entity.enums;

import lombok.Getter;

/**
 * 优惠券领取类型 1.用户领取 2.时间周期
 */
@Getter
public enum CouponClaimedTypeEnum {

    /**
     * 优惠券领取类型
     */
    USER_LIMIT(1,"用户领取"),
    TIME_LIMIT(2,"时间周期");

    private Integer code;
    private String desc;

    CouponClaimedTypeEnum(Integer code, String desc) {
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
        for (CouponClaimedTypeEnum enums : CouponClaimedTypeEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums.getDesc();
            }
        }
        return "";
    }

}
