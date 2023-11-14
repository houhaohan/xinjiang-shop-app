package com.pinet.rest.mq.consumer;


import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.service.ICustomerCouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program: xinjiang-shop-app
 * @description: 优惠券
 * @author: hhh
 * @create: 2023-06-30 15:03
 **/
@Component
@Slf4j
public class CouponListener {
    @Resource
    private ICustomerCouponService customerCouponService;


    /**
     * 优惠券过期提醒
     *
     * @param message
     */
    @JmsListener(destination = QueueConstants.QING_COUPON_EXPIRE_WARN_NAME, containerFactory = "queueListener")
    public void couponExpire(String message) {
        customerCouponService.pushCouponExpireMsg(Long.valueOf(message));
    }
}
