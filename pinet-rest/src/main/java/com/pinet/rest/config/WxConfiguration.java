package com.pinet.rest.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.pinet.rest.config.properties.WeiXinAppProperties;
import com.pinet.rest.config.properties.WeiXinMiniProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @program: xinjiang-shop-app
 * @description: 微信支付配置类
 * @author: hhh
 * @create: 2022-12-19 16:12
 **/
@Configuration
@Slf4j
public class WxConfiguration {
    @Resource
    private WeiXinMiniProperties weiXinMiniProperties;

    @Resource
    private WeiXinAppProperties weiXinAppProperties;

    @Bean(name = "miniPayService")
    public WxPayService wxMiniPayService() {
        //实例payConfig 设置固定参数
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(StringUtils.trimToNull(this.weiXinMiniProperties.getAppid()));
        payConfig.setMchId(StringUtils.trimToNull(this.weiXinMiniProperties.getMchId()));
        payConfig.setMchKey(StringUtils.trimToNull(this.weiXinMiniProperties.getKey()));
        payConfig.setKeyPath(StringUtils.trimToNull(this.weiXinMiniProperties.getP12url()));

        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }


    @Bean(name = "appPayService")
    public WxPayService wxAppPayService(){
        //实例payConfig 设置固定参数
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(StringUtils.trimToNull(this.weiXinAppProperties.getAppid()));
        payConfig.setMchId(StringUtils.trimToNull(this.weiXinAppProperties.getMchId()));
        payConfig.setMchKey(StringUtils.trimToNull(this.weiXinAppProperties.getKey()));
        payConfig.setKeyPath(StringUtils.trimToNull(this.weiXinAppProperties.getP12url()));

        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    @Bean
    public WxMaService wxMaService() {
        WxMaConfig wxMaConfig = new WxMaDefaultConfigImpl();
        ((WxMaDefaultConfigImpl) wxMaConfig).setAppid(StringUtils.trimToNull("wx89ad3dd778a3e6ff"));
        ((WxMaDefaultConfigImpl) wxMaConfig).setSecret(StringUtils.trimToNull("4e0c7a50e7c358eb290cc994ee03766d"));
        WxMaService wxMaService = new WxMaServiceImpl();
        //设置配置文件
        wxMaService.setWxMaConfig(wxMaConfig);
        return wxMaService;
    }
}
