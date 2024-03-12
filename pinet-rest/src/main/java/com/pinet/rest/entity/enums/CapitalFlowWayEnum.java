package com.pinet.rest.entity.enums;

/**
 * @program: wlbz-cms
 * @description: 资金流水到账方式
 * @author: hhh
 * @create: 2023-07-11 14:39
 **/
public enum CapitalFlowWayEnum {
    /**
     * 到账方式
     */
    ZFB(1,"支付宝"),
    WE_CHAT(2,"微信"),
    BANK(3,"银行卡");

    private Integer code;
    private String msg;

    CapitalFlowWayEnum() {
    }

    CapitalFlowWayEnum(Integer code, String msg) {
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
        for (CapitalFlowWayEnum enums : CapitalFlowWayEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums.getMsg();
            }
        }

        return "";
    }


    public static CapitalFlowWayEnum getEnumByChannelId(String channelId){
        if ("alipay_app".equals(channelId)){
            return CapitalFlowWayEnum.ZFB;
        }else {
            return CapitalFlowWayEnum.WE_CHAT;
        }
    }
}
