package com.pinet;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 */

@SpringBootApplication
@MapperScan("com.pinet.rest.mapper")
public class PinetApplication {

    public static void main(String[] args) {
        SpringApplication.run(PinetApplication.class, args);
    }

}
