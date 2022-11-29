package com.pinet.pay.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class WxPayConfig {
    /** 商户号 */
    public static String merchantId = "1901174254";
    /** 商户API私钥路径 */
    public static String privateKeyPath = "apiclient_key.pem";
    /** 商户证书序列号 */
    public static String merchantSerialNumber = "54776TTTF8F77EXXX3641FAB5F940FII11C65347";
    /** 微信支付平台证书路径 */
    public static String wechatPayCertificatePath = "wechatpay_3A4AF69999DF01F39BB08C21C1C29B6AA17C074N.pem";
    /** 微信支付 APIv3 密钥 */
    /** 如果微信支付平台证书，已经下载好了，apiV3Key 就不需要了 */
    public static String apiV3Key = "B3AQsC17C6UFooIRCAaXRUvaq8PInN60";
}
