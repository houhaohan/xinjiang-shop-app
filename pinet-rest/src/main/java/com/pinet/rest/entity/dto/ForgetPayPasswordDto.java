package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: xinjiang-shop-app
 * @description: 忘记密码dto
 * @author: hhh
 * @create: 2023-09-14 10:56
 **/
@Data
public class ForgetPayPasswordDto {
    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty("验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;

    @ApiModelProperty("支付密码")
    @NotBlank(message = "支付密码不能为空")
    private String payPassword;
}
