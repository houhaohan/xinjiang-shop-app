package com.pinet.rest.entity.vo;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.pinet.rest.entity.Customer;
import com.pinet.rest.entity.CustomerMember;
import com.pinet.rest.entity.VipUser;
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

    @ApiModelProperty("微信session信息")
    private WxMaJscode2SessionResult sessionInfo;
}
