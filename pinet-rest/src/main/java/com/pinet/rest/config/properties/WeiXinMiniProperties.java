package com.pinet.rest.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @program: xinjiang-shop-app
 * @description: 微信小程序
 * @author: hhh
 * @create: 2022-12-19 16:08
 **/
@Data
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "social.weixin.mini")
public class WeiXinMiniProperties {
    private String appid;

    private String secret;

    private String mchId;

    private String notifyUrl;

    private String key;

    private String p12url;
}
