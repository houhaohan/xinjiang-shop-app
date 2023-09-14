package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: xinjiang-shop-app
 * @description: 设置支付密码dto
 * @author: hhh
 * @create: 2023-09-14 09:44
 **/
@Data
public class SetPayPasswordDto {
    @ApiModelProperty(value = "支付密码")
    @NotBlank(message = "支付密码不能为空")
    private String payPassword;

    @ApiModelProperty(value = "旧密码(仅修改支付密码接口传此字段)")
    private String oldPayPassword;
}
