package com.pinet.log.config;

import com.pinet.log.annotation.Log;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(Log.class)
public class LogConfig {


}
