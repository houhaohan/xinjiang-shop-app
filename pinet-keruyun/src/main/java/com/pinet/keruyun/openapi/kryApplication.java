package com.pinet.keruyun.openapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
public class kryApplication {
    public static void main(String[] args) {
        log.info("------------------------service is start--------------------");
        SpringApplication.run(kryApplication.class, args);
        log.info("------------------------service is end ---------------------");
    }
}
