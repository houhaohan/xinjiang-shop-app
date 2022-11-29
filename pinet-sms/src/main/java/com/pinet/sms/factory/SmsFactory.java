package com.pinet.sms.factory;

import com.pinet.sms.SmsInterface;
import com.pinet.sms.enums.SmsType;
import com.pinet.sms.sms.ChuangLanSms;
import java.util.HashMap;
import java.util.Map;


public class SmsFactory {
	
	private static Map<SmsType, SmsInterface> sms = new HashMap<>();

	public static SmsInterface createChangLan(){
		return create(SmsType.ChuangLan);
	}

	public static SmsInterface create(SmsType type){
		if(sms.get(type)==null){
			SmsInterface smsClient = null;
			switch(type){
				case ChuangLan:
					smsClient = new ChuangLanSms();
					break;
			}
			sms.put(type, smsClient);
		}
		return sms.get(type);
	}

}
