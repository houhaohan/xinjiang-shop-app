package com.pinet.pay.service.impl;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.Refund;

/** JSAPI 下单为例 */
public class QuickStart {

    /** 商户号 */
    public static String merchantId = "";
    /** 商户API私钥路径 */
    public static String privateKeyPath = "";
    /** 商户证书序列号 */
    public static String merchantSerialNumber = "";
    /** 微信支付平台证书路径 */
    public static String wechatPayCertificatePath = "";
    /** 微信支付 APIv3 密钥 */
    public static String apiV3Key = "";

    public static void main(String[] args) {
        Config config =
                new RSAConfig.Builder()
                        .merchantId(merchantId)
                        .privateKeyFromPath(privateKeyPath)
                        .merchantSerialNumber(merchantSerialNumber)
                        .wechatPayCertificatesFromPath(wechatPayCertificatePath)
                        .build();
        JsapiService service = new JsapiService.Builder().config(config).build();
        // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(100);
        request.setAmount(amount);
        request.setAppid("wxa9d9651ae******");
        request.setMchid("190000****");
        request.setDescription("测试商品标题");
        request.setNotifyUrl("https://notify_url");
        request.setOutTradeNo("out_trade_no_001");
        Payer payer = new Payer();
        payer.setOpenid("oLTPCuN5a-nBD4rAL_fa********");
        request.setPayer(payer);
        PrepayResponse response = service.prepay(request);
        System.out.println(response.getPrepayId());


        //退款
        RefundService refundService = new RefundService.Builder().config(config).build();
        CreateRequest request1 = new CreateRequest();
        Refund refund = refundService.create(request1);
        System.out.println(refund.getAmount());

    }
}
