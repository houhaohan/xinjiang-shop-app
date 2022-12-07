package com.pinet.rest.service;

import com.pinet.rest.entity.vo.SmsVo;
import com.pinet.sms.enums.SmsTemplate;

public interface ISmsService {

    SmsVo send(String phone, SmsTemplate smsTemplate);
}
