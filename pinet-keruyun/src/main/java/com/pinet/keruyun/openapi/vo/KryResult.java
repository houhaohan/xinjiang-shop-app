package com.pinet.keruyun.openapi.vo;

import lombok.Data;

@Data
public class KryResult<T> {
    private T value;
    private boolean success;
    private String errorDesc;
    private Integer serverCode;
    private String messageId;

}
