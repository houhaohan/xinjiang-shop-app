package com.pinet.rest.entity.enums;

/**
 * @program: wlbz
 * @description: 资金流水表枚举
 * @author: hhh
 * @create: 2022-12-02 14:42
 **/
public enum BalanceRecordTypeEnum {
    /**
     * 订单状态
     */
    _1(1,"资源通任务增加"),
    _2(2,"充值增加"),
    _3(3,"轻食下单消费扣减"),
    _4(4,"提现扣减"),
    _5(5,"充值"),
    _6(6,"店帮主佣金到账"),
    _7(7,"轻食下单消费退款");


    private Integer code;
    private String msg;

    BalanceRecordTypeEnum() {
    }

    BalanceRecordTypeEnum(Integer code, String msg) {
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
        for (BalanceRecordTypeEnum enums : BalanceRecordTypeEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums.getMsg();
            }
        }

        return "";
    }
}
