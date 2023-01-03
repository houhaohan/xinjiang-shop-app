package com.pinet.rest.controller.login;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.pinet.core.result.Result;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.request.WxLoginRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/{version}/wx")
@RestController
@Api(tags = "微信")
@Slf4j
public class WxController {

    @Autowired
    private WxMaService wxMaService;

    /**
     * TODO 获取手机号的code 和 登入的code 不是同一个code
     * 获取微信手机号
     * @param request
     * @return
     */
    @RequestMapping(value = "/getPhoneNumber",method = RequestMethod.POST)
    @ApiOperation("获取微信手机号")
    @NotTokenSign
    public Result<String> getPhoneNumber(@Validated @RequestBody WxLoginRequest request){
        try {
            WxMaUserService userService = wxMaService.getUserService();
            WxMaJscode2SessionResult sessionInfo = userService.getSessionInfo(request.getCode());
            WxMaPhoneNumberInfo phoneNumberInfo = userService.getPhoneNoInfo(sessionInfo.getSessionKey(), request.getEncryptedData(), request.getIv());
            return Result.ok(phoneNumberInfo.getPhoneNumber());
        }catch (WxErrorException e){
            log.error(e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return Result.error("操作失败");

    }
}
