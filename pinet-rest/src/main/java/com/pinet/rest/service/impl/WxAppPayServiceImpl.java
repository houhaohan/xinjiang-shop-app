package com.pinet.rest.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.IPUtils;
import com.pinet.rest.config.properties.WeiXinAppProperties;
import com.pinet.rest.entity.param.PayParam;
import com.pinet.rest.entity.param.RefundParam;
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
                .attach(param.getPayType().toString())
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

    @Override
    public void refund(RefundParam param) {
        WxPayRefundRequest wxPayRefundRequest = WxPayRefundRequest.newBuilder()
                //订单总金额(分)
                .totalFee(BaseWxPayRequest.yuanToFen(param.getTotalFee()))
                //订单编号
                .outTradeNo(param.getOrderNo())
                //退款编号
                .outRefundNo(param.getOutRefundNo())
                //退款金额(分)
                .refundFee(BaseWxPayRequest.yuanToFen(param.getRefundFee()))
                //回调接口
                .notifyUrl(weiXinAppProperties.getRefundNotifyUrl())
                .build();

        try {
            appPayService.refund(wxPayRefundRequest);
        } catch (Exception e) {
            log.error("app退款申请失败,失败原因{}",e);
        }
    }
}
