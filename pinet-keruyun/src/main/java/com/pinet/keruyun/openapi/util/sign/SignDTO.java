package com.pinet.keruyun.openapi.util.sign;

import com.pinet.keruyun.openapi.type.AuthType;
import com.pinet.keruyun.openapi.type.Method;
import lombok.Builder;
import lombok.Data;

/**
 * @author zhaobo
 */
@Data
@Builder
public class SignDTO {
    /**
     * 应用key
     */
    private String appKey;

    /**
     * BRAND：授权品牌调用
     * SHOP：授权门店调用
     */
    private AuthType authType;
    /**
     * 品牌id（根据authType，传递对应品牌或者门店id）
     */
    private Long orgId;
    /**
     * 签名版本号（默认2.0版本）
     */
    private String version;
    /**
     * 时间戳（单位秒）
     */
    private Long timestamp;

    /**
     * 请求类型
     */
    private Method method;
    /**
     * 请求业务参数
     */
    private Object requestBody;
}
