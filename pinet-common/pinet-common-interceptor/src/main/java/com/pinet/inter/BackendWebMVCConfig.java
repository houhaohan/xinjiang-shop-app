package com.pinet.inter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class BackendWebMVCConfig implements WebMvcConfigurer {
    @Resource
    private BackendInterceptor backendInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(backendInterceptor)
                //默认拦截所有请求
                .addPathPatterns("/**")
                //不需要拦截的请求 swagger
                .excludePathPatterns("/swagger-resources/**","/swagger-ui/**","/v3/**")
                // 登入接口
                .excludePathPatterns("/login/**");
    }
}
