package com.pinet.rest.mq.consumer;


import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.core.date.DateUtil;
import com.pinet.core.constants.CommonConstant;
import com.pinet.rest.entity.Customer;
import com.pinet.rest.entity.CustomerCoupon;
import com.pinet.rest.entity.enums.CouponTypeEnum;
import com.pinet.rest.entity.enums.WeChatTemplateEnum;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.service.ICustomerCouponService;
import com.pinet.rest.service.WechatTemplateMessageDeliver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * @program: xinjiang-shop-app
 * @description: 优惠券
 * @author: hhh
 * @create: 2023-06-30 15:03
 **/
@Component
@RequiredArgsConstructor
@Slf4j
public class CouponListener {

    private final ICustomerCouponService customerCouponService;

    private final WechatTemplateMessageDeliver wechatTemplateMessageDeliver;


    /**
     * 优惠券过期提醒
     *
     * @param message
     */
    @JmsListener(destination = QueueConstants.QING_COUPON_EXPIRE_WARN_NAME, containerFactory = "queueListener")
    public void sendTemplateMessage(String message) {
        try {
            String templateId = WeChatTemplateEnum.COUPON_EXPIRE.getKey();
            String url = WeChatTemplateEnum.COUPON_EXPIRE.getPageUrl();

            CustomerCoupon customerCoupon = customerCouponService.getById(Long.valueOf(message));
            //温馨提示
            String tips = "你的优惠券将在1天后过期,请尽快使用";
            String couponName = customerCoupon.getCouponName();
            String couponType = CouponTypeEnum.getDescByCode(customerCoupon.getCouponType());
            String expireTime = DateUtil.format(customerCoupon.getExpireTime(), "yyyy-MM-dd HH:mm:ss");

            List<WxMaSubscribeMessage.MsgData> data = new ArrayList<>(4);
            //优惠券名称
            data.add(new WxMaSubscribeMessage.MsgData("thing10", couponName));
            //优惠券类型
            data.add(new WxMaSubscribeMessage.MsgData("thing8", couponType));
            //过期时间
            data.add(new WxMaSubscribeMessage.MsgData("time4", expireTime));
            //温馨提示
            data.add(new WxMaSubscribeMessage.MsgData("thing6", tips));
            wechatTemplateMessageDeliver.asyncSend(templateId,url,customerCoupon.getCustomerId(),data);
        } catch (WxErrorException e) {
            log.error("优惠券过期提醒发送失败,error{}", e);
        }
    }
}
