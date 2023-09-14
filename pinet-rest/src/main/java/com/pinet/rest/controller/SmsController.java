package com.pinet.rest.controller;


import com.pinet.core.constants.CommonConstant;
import com.pinet.core.enums.ErrorCodeEnum;
import com.pinet.core.result.Result;
import com.pinet.core.version.ApiVersion;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.request.SmsSendRequest;
import com.pinet.rest.entity.response.SmsSendResponse;
import com.pinet.rest.service.ISmsService;
import com.pinet.sms.enums.SmsTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        SmsTemplate smsTemplate = SmsTemplate.getTemplateByName(request.getType());
        if(smsTemplate == null){
            return Result.error(ErrorCodeEnum.FAILED);
        }
        SmsSendResponse result = smsService.send(request.getPhone(), smsTemplate, CommonConstant.SMS_CODE_LOGIN);
        if("0".equals(result.getCode())){
            return Result.ok(result);
        }
        return Result.error(result.getErrorMsg());
    }


    @ApiOperation("发送忘记支付密码验证码")
    @RequestMapping(value = "/sendForgetPayPasswordCode",method = RequestMethod.POST)
    @ApiVersion(1)
    public Result<?> sendForgetPayPasswordCode(@Validated @RequestBody SmsSendRequest request){
        SmsTemplate smsTemplate = SmsTemplate.getTemplateByName(request.getType());
        if(smsTemplate == null){
            return Result.error(ErrorCodeEnum.FAILED);
        }
        SmsSendResponse result = smsService.send(request.getPhone(), smsTemplate,CommonConstant.SMS_CODE_FORGET_PAY_PASSWORD);
        if("0".equals(result.getCode())){
            return Result.ok(result);
        }
        return Result.error(result.getErrorMsg());
    }
}
