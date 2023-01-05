package com.pinet.rest.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @program: xinjiang-shop-app
 * @description: 微信小程序
 * @author: hhh
 * @create: 2022-12-19 16:08
 **/
@Configuration
@ConfigurationProperties(prefix = "social.weixin.mini")
public class WeiXinMiniProperties {
    private String appid;

    private String secret;

    private String mchId;

    private String notifyUrl;

    private String key;

    private String p12url;

    private String refundNotifyUrl;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getP12url() {
        return p12url;
    }

    public void setP12url(String p12url) {
        this.p12url = p12url;
    }

    public String getRefundNotifyUrl() {
        return refundNotifyUrl;
    }

    public void setRefundNotifyUrl(String refundNotifyUrl) {
        this.refundNotifyUrl = refundNotifyUrl;
    }
}
