package com.pinet.keruyun.openapi;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 */

@SpringBootApplication
@Slf4j
public class KryApplication {

    public static void main(String[] args) {
        log.info("------------------------service is start--------------------");
        SpringApplication.run(KryApplication.class, args);
        log.info("------------------------service is end ---------------------");
    }

}
