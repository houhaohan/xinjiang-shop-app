package com.pinet.rest.entity.enums;

import lombok.Getter;


@Getter
public enum CouponReceiveStatusEnum {
    /**
     * 优惠券状态
     */
    NOT_RECEIVE(1,"未领取"),
    RECEIVED(2,"已领取"),
    GIVE_UP(3,"已放弃"),
    USED(4,"已使用");

    private Integer code;
    private String desc;

    CouponReceiveStatusEnum(Integer code, String desc) {
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
        for (CouponReceiveStatusEnum enums : CouponReceiveStatusEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums.getDesc();
            }
        }
        return "";
    }
}
