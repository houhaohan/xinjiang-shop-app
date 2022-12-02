package com.pinet.rest.entity.enums;

/**
 * @program: xinjiang-shop-app
 * @description: 订单状态enum
 * @author: hhh
 * @create: 2022-12-02 14:42
 **/
public enum OrderStatusEnum {
    /**
     * 订单状态
     */
    NOT_PAY(10,"待付款"),
    PAY_COMPLETE(20,"已支付(已下单)"),
    MAKE(30,"制作中"),
    SEND_OUT(40,"配送中"),
    DELIVERY(50,"已送达"),
    REFUND(90,"已退款"),
    CANCEL(99,"已取消"),
    COMPLETE(100,"已完成");


    private Integer code;
    private String msg;

    OrderStatusEnum() {
    }

    OrderStatusEnum(Integer code, String msg) {
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
        for (OrderStatusEnum enums : OrderStatusEnum.values()) {
            if (enums.getCode().equals(code)) {
                return enums.getMsg();
            }
        }

        return "";
    }
}
