package com.pinet.rest.entity.enums;

import java.math.BigDecimal;

/**
 * @program: xinjiang-shop-app
 * @description: 订单状态enum
 * @author: hhh
 * @create: 2022-12-02 14:42
 **/
public enum MemberLevelEnum {
    /**
     * 订单状态
     */
    _0(0,"门客",new BigDecimal("1")),
    _10(10,"会员",new BigDecimal("0.85")),
    _20(20,"店帮主",new BigDecimal("0.7"));


    private Integer code;
    private String msg;
    //折扣
    private BigDecimal discount;

    MemberLevelEnum() {
    }

    MemberLevelEnum(Integer code, String msg, BigDecimal discount) {
        this.code = code;
        this.msg = msg;
        this.discount = discount;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    /**
     * 通过code取msg
     *
     * @param code 值
     * @return
     */
    public static String getMsgByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (MemberLevelEnum enums : MemberLevelEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums.getMsg();
            }
        }

        return "";
    }


    /**
     * 通过code取enum
     *
     * @param code 值
     * @return
     */
    public static MemberLevelEnum getEnumByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (MemberLevelEnum enums : MemberLevelEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums;
            }
        }

        return null;
    }
}
