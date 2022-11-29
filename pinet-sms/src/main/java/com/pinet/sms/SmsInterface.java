package com.pinet.sms;

import com.pinet.core.result.Result;
import com.pinet.sms.enums.SmsTemplate;
import java.util.Map;

public interface SmsInterface {

	Result send(String phones, SmsTemplate smsTemplate, Map<String, String> data);

	Result sendPickupOrderNoticeForShop(String phone, SmsTemplate smsTemplate, Map<String, String> data);

	Result sendPickupOrderNoticeForUser(String phone, SmsTemplate smsTemplate, Map<String, String> data);
}
