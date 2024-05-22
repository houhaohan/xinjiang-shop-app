package com.pinet.core;

/**
 * 一般原则是：
 * 1. 经常或共性的错误以enum方式写在该类中
 * 2. 200/40x/30x/50x 以系统默认为主，除非特定需要
 * 3. 90x开头的代表系统级别异常，为了辨识度，而设定的返回异常结果
 * 4. 100x开头的代表 内部服务参数校验或日常问题返回
 * 5. 110x开头的代表  限流或其他事务级异常
 * 6. 120x开头代表  API接口返回参数
 */
public enum ApiErrorEnum {
    ERROR_NOT_FOUNT(404, "访问地址未找到"),
    ERROR(500, "系统出错了"),
//    ERROR_NULL_POINT(901, "出现空指针异常"),
    ERROR_NULL_POINT(901, "网络异常,请稍后重试"),
    ERROR_REQUEST_PARAMS_INVALID(1001, "参数无效"),
    ERROR_SYSTEM_IS_NOT_EXIST(1002, "业务系统不存在"),
    ERROR_SERVICE_UNAUTHORIZED(1003, "服务未被授权"),
    ERROR_TOKEN_IS_NULL(1005, "缺乏参数token，禁止访问"),
    ERROR_ACCESS_TOKEN_INVALID(1004, "访问令牌无效"),
    ERROR_EXCEED_MAX_TIMES(1100, "超过最大次数"),
    ERROR_INTERNAL_SERVER_ERROR(999, "服务器内部错误"),
    ERROR_TO_INDEX(1006, "最近店铺没有该商品，请跳转首页购买其他商品"),

    PARAM_APPID_NULL_ERROR(1200, "appid参数不能为空"),
    PARAM_APPID_NOT_EXSIST_ERROR(1201, "appid参数不存在"),
    PARAM_T_ERROR(1202, "请求参数不能为空"),
    PARAM_KEY_ERROR(1203, "appSecret参数不能为空"),
    PARAM_SIGN_ERROR(1204, "sign参数不能为空"),
    PARAM_AUTH_ERROR(1205, "验证签名错误"),
    QUERY_RESULT_IS_EMPTY(1500, "调用过于频繁，请稍后重试"),

    APPLY_SHARDING_LOCK_ERROR(666, "获取锁失败");
    private Integer code;
    private String msg;

    ApiErrorEnum(int code, String msg) {
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
}
