package com.pinet.core.enums;

public enum LoginEnum {

    //1-微信登入，2-验证码登入
    WX(1),
    PHONE_CODE(2);

    private Integer code;

    LoginEnum(Integer code){
        this.code = code;
    }

    public Integer getCode(){
        return code;
    }
}
