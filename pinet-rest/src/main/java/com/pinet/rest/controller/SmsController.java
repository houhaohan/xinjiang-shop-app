package com.pinet.rest.controller;


import com.pinet.core.enums.ErrorCodeEnum;
import com.pinet.core.result.Result;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.entity.dto.SmsDto;
import com.pinet.rest.entity.vo.SmsVo;
import com.pinet.rest.service.ISmsService;
import com.pinet.sms.enums.SmsTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms")
@Api(tags = "短信模块")
public class SmsController {
    @Autowired
    private ISmsService smsService;

    @ApiOperation("发送短息验证码")
    @RequestMapping(value = "/sendCode",method = RequestMethod.POST)
    @NotTokenSign
    public Result sendSmsCode(@RequestBody SmsDto smsDto){
        SmsTemplate smsTemplate = SmsTemplate.getTemplateByName(smsDto.getType());
        if(smsTemplate == null){
            return Result.error(ErrorCodeEnum.FAILED);
        }
        SmsVo result = smsService.send(smsDto.getPhone(), smsTemplate);
        return Result.ok(result);
    }
}
