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

    private static String url = "jdbc:mysql://192.168.1.91:3306/qingshi?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false";
//    private static String url = "jdbc:mysql://192.168.1.91:3306/xjs?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false";
    private static String username = "root";
    private static String password = "123456";
    private static String[] tables = {"kry_order_compensate","kry_order_push_log"};

    public static void main(String[] args) {
        new CodeGenerationManager(url, username, password, tables).execute();
    }
}
