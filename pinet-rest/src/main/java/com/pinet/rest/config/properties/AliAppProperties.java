package com.pinet.rest.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @program: xinjiang-shop-app
 * @description: 阿里app支付
 * @author: hhh
 * @create: 2022-12-27 14:21
 **/
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "social.alipay.app")
public class AliAppProperties {
    private String appid;

    private String privateKey;

    private String publicKey;

    private String publicKeyAlipay;

    private String notifyUrl;

    private String sellerid;

    private String partnerid;

    private String appCertUrl;

    private String rootCertUrl;

    private String publicCertUrl;

    public String getAppCertUrl() {
        return appCertUrl;
    }

    public void setAppCertUrl(String appCertUrl) {
        this.appCertUrl = appCertUrl;
    }

    public String getRootCertUrl() {
        return rootCertUrl;
    }

    public void setRootCertUrl(String rootCertUrl) {
        this.rootCertUrl = rootCertUrl;
    }

    public String getPublicCertUrl() {
        return publicCertUrl;
    }

    public void setPublicCertUrl(String publicCertUrl) {
        this.publicCertUrl = publicCertUrl;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKeyAlipay() {
        return publicKeyAlipay;
    }

    public void setPublicKeyAlipay(String publicKeyAlipay) {
        this.publicKeyAlipay = publicKeyAlipay;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
