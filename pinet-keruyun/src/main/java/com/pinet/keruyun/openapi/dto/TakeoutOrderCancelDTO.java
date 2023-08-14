package com.pinet.keruyun.openapi.dto;

import lombok.Data;

@Data
public class TakeoutOrderCancelDTO {

    private String orderId;
    private String reason;
}
