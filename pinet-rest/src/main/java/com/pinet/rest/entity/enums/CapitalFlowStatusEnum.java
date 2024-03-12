package com.pinet.rest.entity.enums;

/**
 * @program: wlbz-cms
 * @description: 资金流水到账方式
 * @author: hhh
 * @create: 2023-07-11 14:39
 **/
public enum CapitalFlowStatusEnum {
    /**
     * 资金状态
     */
    SUCCESS(1,"交易成功"),
    REFUND(2,"商家退款"),
    WITHDRAWAL(3,"提现扣减");

    private Integer code;
    private String msg;

    CapitalFlowStatusEnum() {
    }

    CapitalFlowStatusEnum(Integer code, String msg) {
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
        for (CapitalFlowStatusEnum enums : CapitalFlowStatusEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums.getMsg();
            }
        }

        return "";
    }
}
