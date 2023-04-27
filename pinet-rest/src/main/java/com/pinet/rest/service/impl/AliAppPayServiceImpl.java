package com.pinet.rest.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundApplyModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.pinet.core.util.DateUtil;
import com.pinet.rest.config.properties.AliAppProperties;
import com.pinet.rest.entity.OrderRefund;
import com.pinet.rest.entity.param.PayParam;
import com.pinet.rest.entity.param.RefundParam;
import com.pinet.rest.service.IOrderRefundService;
import com.pinet.rest.service.IPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

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
        String orderStr="";
        try {
            //构造client
            CertAlipayRequest request = new CertAlipayRequest();
            //设置网关地址
            request.setServerUrl(REFUND_URL);
            //设置应用Id
            request.setAppId(aliAppProperties.getAppid());
            //设置应用私钥
            request.setPrivateKey(aliAppProperties.getPrivateKey());
            //设置请求格式，固定值json
            request.setFormat("json");
            //设置字符集
            request.setCharset("UTF-8");
            //设置签名类型
            request.setSignType("RSA2");
            //设置应用公钥证书路径
            request.setCertPath(aliAppProperties.getAppCertUrl());
            //设置支付宝公钥证书路径
            request.setAlipayPublicCertPath(aliAppProperties.getPublicCertUrl());
            //设置支付宝根证书路径
            request.setRootCertPath(aliAppProperties.getRootCertUrl());
            AlipayClient alipayClient = new DefaultAlipayClient(request);

            AlipayTradeAppPayRequest appPayRequest = new AlipayTradeAppPayRequest();
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setBody(param.getPayDesc());
            model.setSubject(param.getOrderNo());
            model.setOutTradeNo(param.getOrderNo());
            model.setSellerId(aliAppProperties.getSellerid());
            model.setTimeoutExpress("30m");
            model.setTotalAmount(param.getPayPrice().toString());
            model.setProductCode("QUICK_MSECURITY_PAY");
            appPayRequest.setBizModel(model);
            appPayRequest.setNotifyUrl(aliAppProperties.getNotifyUrl());
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(appPayRequest);
            orderStr = response.getBody();
        } catch (AlipayApiException e) {
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
                orderRefund.setOutTradeNo(response.getTradeNo());
                orderRefundService.updateById(orderRefund);
            }
        } catch (Exception e) {
            log.error("支付宝退款出现异常{}",e);
        }


    }
}
