package com.pinet.core.enums;

import lombok.Getter;

@Getter
public enum ApiExceptionEnum {
    FAILED(500,"操作失败"),
    COUPON_EXPIRED(6001,"优惠券未生效"),
    COUPON_NOT_STARTED(6002,"优惠券失效"),
    COUPON_NO_QUANTITY(6003,"优惠券已领完"),
    COUPON_RECEIVE_UPPER_LIMIT(6003,"您领取的数量已达上限，请勿重复操作"),
    ;

    private int code;
    private String message;

    private ApiExceptionEnum(int code, String message){
        this.code = code;
        this.message = message;
    }

    public static ApiExceptionEnum get(int code){
        for (ApiExceptionEnum enums: ApiExceptionEnum.values()){
            if (enums.getCode() == code){
                return enums;
            }
        }
        return null;
    }

}
