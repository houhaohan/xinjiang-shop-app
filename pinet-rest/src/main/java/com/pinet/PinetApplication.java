package com.pinet;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 */

@SpringBootApplication
@ComponentScan(basePackages = {"cn.binarywang","com.pinet"})
@MapperScan("com.pinet.rest.mapper")
@Slf4j
@EnableJms
@EnableScheduling
public class PinetApplication {

    public static void main(String[] args) {
        log.info("------------------------service is start--------------------");
        SpringApplication.run(PinetApplication.class, args);
        log.info("------------------------service is end ---------------------");
    }

}
