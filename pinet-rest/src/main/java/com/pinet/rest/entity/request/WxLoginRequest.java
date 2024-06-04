package com.pinet.rest.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "WxLoginRequest",description = "微信登入参数")
public class WxLoginRequest extends LoginRequest {

    @ApiModelProperty(value = "code")
    private String code;

    @NotBlank(message = "参数encryptedData不能为空")
    @ApiModelProperty(value = "encryptedData",required = true,notes = "获取手机号得到的encryptedData")
    private String encryptedData;

    @NotBlank(message = "参数iv不能为空")
    @ApiModelProperty(value = "iv",required = true,notes = "获取手机号的iv")
    private String iv;

    @ApiModelProperty("用户名")
    private String nickName;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("性别： 0：未知、1：男、2：女")
    private Integer gender;

    @ApiModelProperty("sessionKey")
    private String sessionKey;

    @ApiModelProperty("openid")
    private String openid;

    @ApiModelProperty(value = "店铺Id",notes = "默认保利店")
    private Long shopId = 24L;
}
