package com.pinet.rest.service;

import com.pinet.rest.entity.response.SmsSendResponse;
import com.pinet.sms.enums.SmsTemplate;

public interface ISmsService {


    SmsSendResponse send(String phone, SmsTemplate smsTemplate,String redisKey);
}
