package com.pinet.rest.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.pinet.rest.config.properties.AliAppProperties;
import com.pinet.rest.entity.param.PayParam;
import com.pinet.rest.service.IPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: xinjiang-shop-app
 * @description: 支付宝app支付
 * @author: hhh
 * @create: 2022-12-27 14:31
 **/
@Service("alipay_app_service")
@Slf4j
public class AliAppPayServiceImpl implements IPayService {
    @Resource
    private AliAppProperties aliAppProperties;

    /**
     * 支付宝网关
     */
    public static final String REFUND_URL = "https://openapi.alipay.com/gateway.do";

    @Override
    public Object pay(PayParam param) {
        String orderStr="";
        try {
            //实例化客户端
            AlipayClient alipayClient = new DefaultAlipayClient(REFUND_URL,aliAppProperties.getAppid(),
                    aliAppProperties.getPrivateKey(), "json", "UTF-8",
                    aliAppProperties.getPublicKeyAlipay(), "RSA2");
            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
            AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
            //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            //描述信息 添加附加数据
            model.setBody(param.getPayDesc());
            // 商户订单号(自动生成)
            model.setOutTradeNo(param.getOrderNo());
            // 支付金额
            model.setTotalAmount(param.getPayPrice().toString());

            model.setSellerId(aliAppProperties.getSellerid());
            model.setSubject(param.getOrderNo());
            request.setBizModel(model);

            // 回调地址
            request.setNotifyUrl(aliAppProperties.getNotifyUrl());
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            orderStr = response.getBody();
            log.info(orderStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderStr;
    }

    @Override
    public String getPayName() {
        return "支付宝支付";
    }
}
