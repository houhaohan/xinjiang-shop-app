package com.pinet.rest.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "SmsLoginRequest",description = "短信验证码登入参数")
public class SmsLoginRequest extends LoginRequest{
    @ApiModelProperty(value = "手机号码",required = true)
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "验证码",required = true)
    private String code;


}
