package com.pinet.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ConfigurationProperties(prefix = "spring.security.auth")
public class WhiteSpaceURLListProperties {
    private List<String> allowUrls=new CopyOnWriteArrayList<>();

    public List<String> getAllowUrls() {
        return allowUrls;
    }

    public void setAllowUrls(List<String> allowUrls) {
        this.allowUrls = allowUrls;
    }
}
