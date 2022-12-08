package com.pinet.rest.entity.response;

import lombok.Data;

@Data
public class SmsSendResponse {
    private String code;
    private String msgId;
    private Long time;
    private String errorMsg;

}
