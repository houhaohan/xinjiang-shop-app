package com.pinet.rest.mq.consumer;

import cn.hutool.core.date.DateUtil;
import com.pinet.common.mq.config.QueueConstants;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @program: xinjiang-shop-app
 * @description: 测试 mq消费
 * @author: hhh
 * @create: 2022-12-09 14:43
 **/
@Component
public class TestListener {

    @JmsListener(destination= QueueConstants.QING_SHI_ORDER_PAY_NAME, containerFactory="queueListener")
    public void customerMsg(String message){
        String date = DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss");
//        System.out.println(+"接收到消息");
        System.out.println(date+"接收到消息："+message);
    }
}
