package com.pinet.rest.entity.vo;

import lombok.Data;

@Data
public class SmsVo {
    private String code;
    private String msgId;
    private Long time;
    private String errorMsg;
}
