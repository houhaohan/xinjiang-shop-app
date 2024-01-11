package com.pinet.core.util;

public class Environment {

    public static boolean isProd(){
        String active = SpringContextUtils.getActiveProfile();
        return "prod".equals(active);
    }
}
