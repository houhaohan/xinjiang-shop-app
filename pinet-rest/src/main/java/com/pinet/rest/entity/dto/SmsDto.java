package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "SmsDto",description = "发送短信验证码参数")
public class SmsDto {
    @ApiModelProperty(value = "手机号码")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "验证码")
    private String code;

    @ApiModelProperty(value = "类型，login-登入")
    @NotBlank(message = "类型不能为空")
    private String type;
}
