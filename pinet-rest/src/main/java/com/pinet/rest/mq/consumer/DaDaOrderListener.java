package com.pinet.rest.mq.consumer;

import com.pinet.core.constants.UserConstant;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.service.ISmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @description: 达达配送订单创建异常 短信通知
 * @author: chengshuanghui
 * @date: 2024-03-12 10:54
 */
@Component
@Slf4j
public class DaDaOrderListener {
    @Autowired
    private ISmsService smsService;


    /**
     * 达达订单创建失败
     * @param message
     */
    @JmsListener(destination = QueueConstants.DELIVERY_ORDER_FAIL_SMS, containerFactory = "queueListener")
    public void sendMsg(String message) {
        Long orderId = Long.valueOf(message);
        String msg = "【食的作品】尊敬的用户，您的订单["+orderId+"]配送创建出现异常，请及时处理。";
        smsService.sendSmsMsg(UserConstant.DEFAULT_NOTICE_PHONE, msg);
    }

    /**
     * 达达订单取消配送短信通知
     * @param message
     */
    @JmsListener(destination = QueueConstants.DELIVERY_ORDER_CANCEL_SMS, containerFactory = "queueListener")
    public void orderCancel(String message) {
        String msg = "【食的作品】尊敬的用户，您的订单["+message+"]配送已取消，请及时处理。";
        smsService.sendSmsMsg(UserConstant.DEFAULT_NOTICE_PHONE, msg);
    }
}
