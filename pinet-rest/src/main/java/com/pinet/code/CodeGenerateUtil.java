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

    private static String url = "jdbc:mysql://47.110.9.224:3965/xinjiang-shop?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false";
//    private static String url = "jdbc:mysql://192.168.1.91:3306/xjs?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false";
    private static String username = "root";
    private static String password = "ypxl.2022-12-09!@#";
    private static String[] tables = {"sys_config"};

    public static void main(String[] args) {
        new CodeGenerationManager(url, username, password, tables).execute();
    }
}
