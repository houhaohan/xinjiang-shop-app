package com.pinet.rest.entity.enums;

import lombok.Getter;


@Getter
public enum CouponStatusEnum {
    /**
     * 优惠券状态
     */
    NOT_STARTED(1,"未开始"),
    IN_PROGRESS(2,"进行中"),
    EXPIRED(3,"已失效");

    private Integer code;
    private String desc;

    CouponStatusEnum(Integer code, String desc) {
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
        for (CouponStatusEnum enums : CouponStatusEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums.getDesc();
            }
        }
        return "";
    }
}
