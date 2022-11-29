package com.pinet.sms.errors;

import com.pinet.sms.enums.ResultEnum;

/**
 * Created by acer on 2018/1/8.
 */
public enum SmsResultEnums implements ResultEnum {

    SMS_EMPTY(-10002, "验证码错误"),
    SMS_ERROR(-10003, "验证码错误"),
    SMS_EXPIRED(-10004,"验证码已过期"),
    OLD_PHONE_EXPIRED(-10005,"原手机验证码已失效，请重发"),
    CONTENT_EMPTY(-10006, "内容不能为空");

    private int code;

    private String message;

    SmsResultEnums(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
