package com.pinet.rest.entity.enums;

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
    _0(0,"门客"),
    _10(10,"会员"),
    _20(20,"店帮主");


    private Integer code;
    private String msg;

    MemberLevelEnum() {
    }

    MemberLevelEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
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
        for (MemberLevelEnum enums : MemberLevelEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums.getMsg();
            }
        }

        return "";
    }
}
