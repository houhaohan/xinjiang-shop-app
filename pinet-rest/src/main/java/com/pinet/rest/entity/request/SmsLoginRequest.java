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

    @ApiModelProperty(value = "手机验证码",required = true)
    @NotBlank(message = "验证码不能为空")
    private String code;

    @ApiModelProperty(value = "微信登入临时code",required = true)
    @NotBlank(message = "wxCode不能为空")
    private String wxCode;

}
