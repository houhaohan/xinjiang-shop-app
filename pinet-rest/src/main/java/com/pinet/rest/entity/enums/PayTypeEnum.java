package com.pinet.rest.entity.enums;

/**
 * @program: xinjiang-shop-zyt
 * @description: 支付记录类型枚举
 * @author: hhh
 * @create: 2023-04-17 09:59
 **/
public enum PayTypeEnum {
    /**
     * 支付类型
     */
    _1(1,"轻食订单支付","order_pay_notify_service"),
    _2(2,"店帮主充值","recharge_notify_service");


    private Integer code;
    private String msg;
    private String service;


    PayTypeEnum() {
    }

    PayTypeEnum(Integer code, String msg, String service) {
        this.code = code;
        this.msg = msg;
        this.service = service;
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

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    /**
     * 通过code取枚举
     *
     * @param code 值
     * @return
     */
    public static PayTypeEnum getEnumByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PayTypeEnum enums : PayTypeEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums;
            }
        }

        return null;
    }
}
