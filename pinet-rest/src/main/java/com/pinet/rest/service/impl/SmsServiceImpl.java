package com.pinet.rest.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.pinet.common.redis.util.RedisUtil;
import com.pinet.rest.service.ISmsService;
import com.yupuxilong.sms.request.SmsSendRequest;
import com.yupuxilong.sms.util.ChuangLanSmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements ISmsService {
    @Autowired
    private RedisUtil redisUtil;

    @Value("${sms.code.expire}")
    private Long expire;

    private static final String URL = "http://smssh1.253.com/msg/v1/send/json";

//    @Override
//    public SmsSendResponse send(String phone, SmsTemplate smsTemplate,String redisKey) {
//        int code = StringUtil.randomSixCode();
//        Map<String,String> codeMap = new HashMap<>();
//        codeMap.put("code",String.valueOf(code));
//        JSONObject sendResult = SmsFactory.createChangLan().send(phone, smsTemplate, codeMap);
//        if("0".equals(sendResult.getString("code"))){
//            //发送成功
//            redisUtil.set(redisKey +phone,String.valueOf(code),expire, TimeUnit.SECONDS);
//        }
//        return sendResult.toJavaObject(SmsSendResponse.class);
//    }


    @Override
    public void sendSmsMsg(String tel, String msg) {
        SmsSendRequest smsSendRequest = new
                SmsSendRequest("N7468275",
                "8yGSghzre", msg,
                tel,
                "true");
        ChuangLanSmsUtil.sendSmsByPost(URL, JSON.toJSONString(smsSendRequest));
    }

    @Override
    public void sendVerificationCode(String tel, String redisKey) {
        Integer code = RandomUtil.randomInt(100000,999999);
        String msg = "你的验证码为:" + code;
        sendSmsMsg(tel,msg);
        //发送成功
        redisUtil.set(redisKey +tel,String.valueOf(code),expire, TimeUnit.SECONDS);
    }

}
