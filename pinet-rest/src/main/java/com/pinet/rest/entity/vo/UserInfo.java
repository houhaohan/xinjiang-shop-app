package com.pinet.rest.entity.vo;

import com.pinet.rest.entity.Customer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserInfo {
    /**
     * 用户信息
     */
    private Customer user;

    /**
     * token
     */
    private String access_token;

    /**
     * 失效时间
     */
    private LocalDateTime expireTime;
}
