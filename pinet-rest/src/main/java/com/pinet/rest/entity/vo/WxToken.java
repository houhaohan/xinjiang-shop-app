package com.pinet.rest.entity.vo;

import lombok.Data;

@Data
public class WxToken {

    /**
     * token
     */
    private String access_token;

    /**
     * token 有效期，单位秒
     */
    private Integer expires_in;
}
