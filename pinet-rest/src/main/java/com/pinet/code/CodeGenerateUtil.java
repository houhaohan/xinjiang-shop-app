package com.pinet.code;

import org.springframework.stereotype.Component;

/**
 * @author chengshuanghui
 * @since 2022年11月20日
 * <p>
 * 代码生成器
 */
@Component
public class CodeGenerateUtil {

    private static String url = "jdbc:mysql://127.0.0.1:3306/xjs?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";
    private static String username = "root";
    private static String password = "xingkong";
    private static String[] tables = {"shop"};

    public static void main(String[] args) {
        new CodeGenerationManager(url, username, password, tables).execute();
    }
}
