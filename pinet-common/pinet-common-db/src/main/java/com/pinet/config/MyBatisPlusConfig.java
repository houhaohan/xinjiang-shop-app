package com.pinet.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
//        // 分页插件
//        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
//        paginationInterceptor.setOverflow(mybatisProperties.getPage().isOverflow());
//        paginationInterceptor.setMaxLimit(mybatisProperties.getPage().getMaxLimit());
//        paginationInterceptor.setOptimizeJoin(mybatisProperties.getPage().isOptimizeJoin());
//        interceptor.addInnerInterceptor(paginationInterceptor);

        return interceptor;
    }

}
