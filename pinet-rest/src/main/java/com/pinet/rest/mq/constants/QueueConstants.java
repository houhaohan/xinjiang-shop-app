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
     * 轻食佣金三天自动到账,消费者在商家后台
     */
    public static final String QING_SHI_ORDER_COMMISSION = "qingshi.order.commission";

    /**
     * 优惠券到期提醒
     */
    public static final String QING_COUPON_EXPIRE_WARN_NAME = "qingshi.coupon.expire.warn.queue";

    /**
     * 轻食自提订单发送短信给指定号码
     */
    public static final String QING_ORDER_SEND_SMS_NAME = "qingshi.order.send.sms.queue";

    /**
     * 轻食外卖订单 配送平台创建订单失败 短信提醒 Delivery
     */
    public static final String DELIVERY_ORDER_FAIL_SMS = "delivery.order.fail.sms.queue";

    /**
     * 轻食外卖订单 配送平台创建订单取消 短信提醒 Delivery
     */
    public static final String DELIVERY_ORDER_CANCEL_SMS = "delivery.order.cancel.sms.queue";

    /**
     * 轻食外卖订单 配送平台创建订单失败 短信提醒 Delivery
     */
    public static final String KRY_ORDER_PUSH = "kry.order.push.queue";

    /**
     * 创建客如云会员
     */
    public static final String KRY_VIP_CREATE = "kry.vip.create.queue";

    /**
     * 消息通知发送,消费者在后台消费
     */
    public static final String MESSAGE_SEND = "message.send.queue";

    /**
     * 自配送订单消息通知，消费者在后台消费
     */
    public static final String ZPS_ORDER_NOTICE = "zps.order.notice.queue";
}
