package com.pinet.rest.service;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import com.pinet.rest.entity.Customer;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 微信消息订阅推送
 * @author chengshuanghui
 */
@Component
@RequiredArgsConstructor
public class WechatTemplateMessageDeliver {

    private final WxMaService wxMaService;
    private final ICustomerService customerService;

    /**
     * 推送订阅消息
     * @param templateId 消息模版ID
     * @param urlReturn
     * @param customerId
     * @param data
     * @throws WxErrorException
     */
    public void asyncSend(String templateId, String urlReturn, Long customerId, List<WxMaSubscribeMessage.MsgData> data) throws WxErrorException {
        Customer customer = customerService.getById(customerId);
        WxMaSubscribeMessage message = WxMaSubscribeMessage.builder()
                .templateId(templateId)
                .data(data)
                .toUser(customer.getQsOpenId())
                .page(urlReturn)
                .build();
        wxMaService.getMsgService().sendSubscribeMsg(message);
    }
}
