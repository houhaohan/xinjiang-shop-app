package com.pinet.rest.entity.vo;

import com.pinet.rest.entity.Customer;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LoginResponse {

    private String accessToken;

    private LocalDateTime expireTime;

    private Customer userInfo;
}
