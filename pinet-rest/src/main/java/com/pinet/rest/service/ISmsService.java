package com.pinet.rest.service;

import com.pinet.rest.entity.response.SmsSendResponse;

public interface ISmsService {


//    SmsSendResponse send(String phone, SmsTemplate smsTemplate,String redisKey);

    /**
     * 根据电话推送短信通知
     * @param tel 电话
     * @param msg 内容
     */
    void sendSmsMsg(String tel,String msg);

    /**
     * 发送短信验证码
     * @param tel 电话
     * @param redisKey redis
     */
    void sendVerificationCode(String tel,String redisKey);
}
