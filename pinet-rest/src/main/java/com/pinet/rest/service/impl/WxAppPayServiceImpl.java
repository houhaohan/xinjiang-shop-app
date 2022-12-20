package com.pinet.rest.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.pinet.core.util.IPUtils;
import com.pinet.rest.config.properties.WeiXinAppProperties;
import com.pinet.rest.entity.param.PayParam;
import com.pinet.rest.service.IPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @program: xinjiang-shop-app
 * @description: 微信app支付
 * @author: hhh
 * @create: 2022-12-20 14:20
 **/
@Service("weixin_app_service")
@Slf4j
public class WxAppPayServiceImpl implements IPayService {
    @Resource
    private WeiXinAppProperties weiXinAppProperties;

    @Resource
    private WxPayService appPayService;

    @Override
    public Object pay(PayParam param){
        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = WxPayUnifiedOrderRequest.newBuilder()
                .outTradeNo(param.getOrderNo())
                .tradeType(WxPayConstants.TradeType.APP)
                .totalFee(BaseWxPayRequest.yuanToFen(param.getPayPrice().toString()))
                .body(param.getPayDesc())
                .spbillCreateIp(IPUtils.getIpAddr())
                .notifyUrl(weiXinAppProperties.getNotifyUrl())
                .timeStart(DateUtil.format(new Date(),"yyyyMMddHHmmss"))
                .build();
        wxPayUnifiedOrderRequest.setSignType(WxPayConstants.SignType.MD5);
        try {
            return appPayService.<WxPayAppOrderResult>createOrder(wxPayUnifiedOrderRequest);
        }catch (Exception e){
            log.error("微信app支付出现异常",e);
            return null;
        }
    }

    @Override
    public String getPayName() {
        return "微信APP支付";
    }
}
