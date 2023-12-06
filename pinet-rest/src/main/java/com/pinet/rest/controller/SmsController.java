package com.pinet.rest.controller;


import com.pinet.core.constants.CommonConstant;
import com.pinet.core.result.Result;
import com.pinet.core.version.ApiVersion;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.request.SmsSendRequest;
import com.pinet.rest.service.ISmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{version}/sms")
@Api(tags = "短信模块")
public class SmsController {
    @Autowired
    private ISmsService smsService;

    @ApiOperation("发送短息验证码")
    @RequestMapping(value = "/sendCode",method = RequestMethod.POST)
    @NotTokenSign
    @ApiVersion(1)
    public Result sendSmsCode(@Validated @RequestBody SmsSendRequest request){
        smsService.sendVerificationCode(request.getPhone(),CommonConstant.SMS_CODE_LOGIN);
        return Result.ok();
    }


    @ApiOperation("发送忘记支付密码验证码")
    @RequestMapping(value = "/sendForgetPayPasswordCode",method = RequestMethod.POST)
    @ApiVersion(1)
    public Result<?> sendForgetPayPasswordCode(@Validated @RequestBody SmsSendRequest request){
        smsService.sendVerificationCode(request.getPhone(),CommonConstant.SMS_CODE_FORGET_PAY_PASSWORD);
        return Result.ok();
    }
}
