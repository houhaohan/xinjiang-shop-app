package com.pinet.rest.entity.enums;

/**
 * @program: wlbz
 * @description: 积分流水类型
 * @author: hhh
 * @create: 2022-12-02 14:42
 **/
public enum ScoreRecordTypeEnum {
    /**
     * 订单状态
     */
    ORDER(1,"下单"),
    REFUND(2,"退款"),
    EXCHANGE(3,"兑换");


    private Integer code;
    private String msg;

    ScoreRecordTypeEnum() {
    }

    ScoreRecordTypeEnum(Integer code, String msg) {
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
        for (ScoreRecordTypeEnum enums : ScoreRecordTypeEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums.getMsg();
            }
        }

        return "";
    }
}
