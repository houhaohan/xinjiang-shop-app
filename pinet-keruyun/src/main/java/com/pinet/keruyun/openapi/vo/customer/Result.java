package com.pinet.keruyun.openapi.vo.customer;

import lombok.Data;

/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-05-09 15:32
 */
@Data
public class Result<T> {
    private boolean success;
    private String msgCode;
    private String msgInfo;
    private T data;
    private Integer serverCode;
    private Integer messageId;
}
