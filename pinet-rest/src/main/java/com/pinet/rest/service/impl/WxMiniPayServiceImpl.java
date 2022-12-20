package com.pinet.rest.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.pinet.core.util.IPUtils;
import com.pinet.rest.config.properties.WeiXinAppProperties;
import com.pinet.rest.config.properties.WeiXinMiniProperties;
import com.pinet.rest.entity.param.PayParam;
import com.pinet.rest.service.IPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @program: xinjiang-shop-app
 * @description: 微信小程序支付
 * @author: hhh
 * @create: 2022-12-20 14:31
 **/
@Service("weixin_mini_service")
@Slf4j
public class WxMiniPayServiceImpl implements IPayService {
    @Resource
    private WeiXinMiniProperties weiXinMiniProperties;

    @Resource
    private WxPayService miniPayService;

    @Override
    public Object pay(PayParam param){
        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = WxPayUnifiedOrderRequest.newBuilder()
                .openid(param.getOpenId())
                .outTradeNo(param.getOrderNo())
                .tradeType(WxPayConstants.TradeType.APP)
                .totalFee(BaseWxPayRequest.yuanToFen(param.getPayPrice().toString()))
                .body(param.getPayDesc())
                .spbillCreateIp(IPUtils.getIpAddr())
                .notifyUrl(weiXinMiniProperties.getNotifyUrl())
                .timeStart(DateUtil.format(new Date(),"yyyyMMddHHmmss"))
                .build();
        wxPayUnifiedOrderRequest.setSignType(WxPayConstants.SignType.MD5);
        try {
            return miniPayService.<WxPayMpOrderResult>createOrder(wxPayUnifiedOrderRequest);
        }catch (Exception e){
            log.error("微信小程序支付出现异常",e);
            return null;
        }
    }

    @Override
    public String getPayName() {
        return "微信小程序支付";
    }
}
