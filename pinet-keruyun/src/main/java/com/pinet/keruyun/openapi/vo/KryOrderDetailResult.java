package com.pinet.keruyun.openapi.vo;

import lombok.Data;

import java.util.Map;

@Data
public class KryOrderDetailResult {
    private Boolean success;
    private String msgCode;
    private String msgInfo;
    private Boolean canRetry;
    private OrderDetailVO data;
    private Map<String,Object> extInfo;
    private Integer serverCode;
    private String messageId;
}
