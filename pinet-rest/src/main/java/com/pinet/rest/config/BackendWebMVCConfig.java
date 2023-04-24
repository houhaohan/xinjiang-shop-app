package com.pinet.rest.config;

import com.pinet.inter.BackendInterceptor;
import com.pinet.rest.handler.HandlerInterceptorBuild;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;

@Configuration
public class BackendWebMVCConfig implements WebMvcConfigurer {
    @Resource
    private BackendInterceptor backendInterceptor;

    @Resource
    private HandlerInterceptorBuild handlerInterceptorBuild;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(backendInterceptor)
                .order(1)
                //默认拦截所有请求
                .addPathPatterns("/**")
                //不需要拦截的请求 swagger
                .excludePathPatterns("/swagger-resources/**","/swagger-ui/**","/v3/**","/webjars/**")
                // 登入接口
                .excludePathPatterns("/v*/login/**","/websocket/**");

        registry.addInterceptor(handlerInterceptorBuild)
                .order(2)
                //默认拦截所有请求
                .addPathPatterns("/**")
                //不需要拦截的请求 swagger
                .excludePathPatterns("/swagger-resources/**","/swagger-ui/**","/v3/**","/webjars/**")
                // 登入接口
                .excludePathPatterns("/v*/login/**","/websocket/**");
    }


}
