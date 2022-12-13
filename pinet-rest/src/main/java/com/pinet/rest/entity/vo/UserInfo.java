package com.pinet.rest.entity.vo;

import com.pinet.rest.entity.Customer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "UserInfo",description = "用户信息")
public class UserInfo {
    /**
     * 用户信息
     */
    @ApiModelProperty("用户信息")
    private Customer user;

    /**
     * token
     */
    @ApiModelProperty("token")
    private String access_token;

    /**
     * 失效时间
     */
    @ApiModelProperty("失效时间")
    private LocalDateTime expireTime;
}
