package com.pinet.keruyun.openapi.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhaobo
 */
@ToString
@Data
@Component
@ConfigurationProperties(prefix = "kry")
public class KryApiParamConfig {
    private String projectVersion;
    private String projectName;

    private String url;
    private String appKey;
    private String appSecret;
}
