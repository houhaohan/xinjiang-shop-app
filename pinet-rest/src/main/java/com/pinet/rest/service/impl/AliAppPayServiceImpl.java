package com.pinet.rest.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundApplyModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.pinet.rest.config.properties.AliAppProperties;
import com.pinet.rest.entity.OrderRefund;
import com.pinet.rest.entity.param.PayParam;
import com.pinet.rest.entity.param.RefundParam;
import com.pinet.rest.service.IOrderRefundService;
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

    @Resource
    private IOrderRefundService orderRefundService;

    /**
     * 支付宝网关
     */
    public static final String REFUND_URL = "https://openapi.alipay.com/gateway.do";

    @Override
    public Object pay(PayParam param) {
        String orderStr = "";
        try {
            //实例化客户端
            AlipayClient alipayClient = new DefaultAlipayClient(REFUND_URL, aliAppProperties.getAppid(),
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

    @Override
    public void refund(RefundParam param) {
        try {
            //实例化客户端
            AlipayClient alipayClient = new DefaultAlipayClient(REFUND_URL, aliAppProperties.getAppid(),
                    aliAppProperties.getPrivateKey(), "json", "UTF-8",
                    aliAppProperties.getPublicKeyAlipay(), "RSA2");
            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            AlipayTradeRefundApplyModel model = new AlipayTradeRefundApplyModel();
            //描述信息 添加附加数据
            //订单编号
            model.setOutTradeNo(param.getOrderNo());
            //退款金额
            model.setRefundAmount(param.getRefundFee());
            model.setOutRequestNo(param.getOutRefundNo());
            model.setRefundReason("商品退款");
            request.setBizModel(model);
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            //判断是否退款成功 更新退款记录状态
            if (response.isSuccess()){
                //更新退款记录状态为已到账
                OrderRefund orderRefund = orderRefundService.getById(param.getOrderRefundId());
                orderRefund.setRefundStatus(2);
                orderRefundService.updateById(orderRefund);
            }
        } catch (Exception e) {
            log.error("支付宝退款出现异常{}",e);
        }


    }
}
