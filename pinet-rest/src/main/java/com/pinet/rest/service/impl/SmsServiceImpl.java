package com.pinet.rest.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.constants.CommonConstant;
import com.pinet.core.util.StringUtil;
import com.pinet.rest.entity.response.SmsSendResponse;
import com.pinet.rest.service.ISmsService;
import com.pinet.sms.enums.SmsTemplate;
import com.pinet.sms.factory.SmsFactory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements ISmsService {
    @Autowired
    private RedisUtil redisUtil;

    @Value("${sms.code.expire}")
    private Long expire;

    @Override
    public SmsSendResponse send(String phone, SmsTemplate smsTemplate,String redisKey) {
        int code = StringUtil.randomSixCode();
        Map<String,String> codeMap = new HashMap<>();
        codeMap.put("code",String.valueOf(code));
        JSONObject sendResult = SmsFactory.createChangLan().send(phone, smsTemplate, codeMap);
        if("0".equals(sendResult.getString("code"))){
            //发送成功
            redisUtil.set(redisKey +phone,String.valueOf(code),expire, TimeUnit.SECONDS);
        }
        return sendResult.toJavaObject(SmsSendResponse.class);
    }

}
