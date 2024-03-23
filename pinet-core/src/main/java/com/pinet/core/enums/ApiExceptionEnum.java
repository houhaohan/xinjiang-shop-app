package com.pinet.core.enums;

import lombok.Getter;

@Getter
public enum ApiExceptionEnum {
    FAILED(500,"操作失败"),
    COUPON_EXPIRED(1001,"优惠券未生效"),
    COUPON_NOT_STARTED(1002,"该优惠券活动未开始"),
    COUPON_NO_QUANTITY(1003,"优惠券已领完，无法再领取"),
    COUPON_RECEIVE_UPPER_LIMIT(1004,"您领取的数量已达上限，请勿重复操作"),
    COUPON_DISABLE(1005,"优惠券已禁用"),
    SPEC_ID_NOT_BLANK(1006,"店铺商品样式id不能为空"),
    SPEC_NOT_EXISTS(1007,"样式不存在"),
    ;

    private int code;
    private String message;

    private ApiExceptionEnum(int code, String message){
        this.code = code;
        this.message = message;
    }

    public static ApiExceptionEnum getByCode(int code){
        for (ApiExceptionEnum enums: ApiExceptionEnum.values()){
            if (enums.getCode() == code){
                return enums;
            }
        }
        return null;
    }

}
