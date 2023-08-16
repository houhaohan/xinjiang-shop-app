package com.pinet.rest.mq.constants;


/**
 * @program: xinjiang-shop-app
 * @description: 队列常量
 * @author: hhh
 * @create: 2022-12-09 14:20
 **/
public class QueueConstants {
    /**
     * 下单15分钟未支付自动取消订单
     */
    public static final String QING_SHI_ORDER_PAY_NAME = "qingshi.order.pay.queue";

    /**
     * 店帮主到期自动设置为会员
     */
    public static final String QING_MEMBER_PAY_NAME = "qingshi.member.pay.queue";

    /**
     * 优惠券到期提醒
     */
    public static final String QING_COUPON_EXPIRE_WARN_NAME = "qingshi.coupon.expire.warn.queue";
}
