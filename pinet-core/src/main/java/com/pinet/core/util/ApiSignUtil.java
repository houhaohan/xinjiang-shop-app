package com.pinet.core.util;

import cn.hutool.core.codec.Base64Encoder;
import com.pinet.core.exception.UguessException;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * API生成签名及验签工具类
 */
public class ApiSignUtil {

    public static String MD5Base64(String s) {
        if (s == null)
            return null;
        String encodeStr = "";
        byte[] utfBytes = s.getBytes();
        MessageDigest mdTemp;
        try {
            mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(utfBytes);
            byte[] md5Bytes = mdTemp.digest();
            Base64Encoder b64Encoder = new Base64Encoder();
            encodeStr = b64Encoder.encode(md5Bytes);
        } catch (Exception e) {
            throw new Error("Failed to generate MD5 : " + e.getMessage());
        }
        return encodeStr;
    }

    /*
     * 计算 HMAC-SHA1
     */
    public static String HMACSha1(String data, String key) {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = (new Base64Encoder()).encode(rawHmac);
        } catch (Exception e) {
            throw new Error("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }

    /**
     * 生成签名
     * @param appId     应用ID
     * @param appSecret  应用Secret
     * @param date  日期  toGMTString(new Date())
     * @param method    post|get|put|delete
     * @param body  请求体
     * @return  sign签名
     */
    public static String sign(String appId,String appSecret,String date,String method,String ...body){
        String reqBody="";
        String accept = "application/json";
        String content_type = "application/json";
        if(body==null || body.length<=0){

        }else{
            if(body.length>1)throw new UguessException("post请求不允许传入多个请求体！");
            // 如果是post，对请求体生成md5摘要
            reqBody=body[0];
        }
        String bodyMD5 = MD5Base64(reqBody);
        String stringToSign = method + "\n" + accept + "\n" + bodyMD5 + "\n" + content_type + "\n" + date;
        // 2.计算 HMAC-SHA1
        String sign = HMACSha1(stringToSign, appSecret);
        return sign;
    }

//    /**
//     * 验证签名
//     * @param appId
//     * @param date
//     * @param method
//     * @param body
//     * @return
//     */
//    public static String verifySign(String appId,String date,String method,String sign,String ...body){
//
//    }




    /*
     * 发送POST请求
     */
    public static String sendPost(String url, String body, String ak_id, String ak_secret) throws Exception {

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        int statusCode = 200;
        try {
            URL realUrl = new URL(url);
            /*
             * http header 参数
             */
            String method = "POST";
            String accept = "application/json";
            String content_type = "application/json";
            String path = realUrl.getFile();
            String date = toGMTString(new Date());
            // 1.对body做MD5+BASE64加密
            String bodyMd5 = MD5Base64(body);
            String stringToSign = method + "\n" + accept + "\n" + bodyMd5 + "\n" + content_type + "\n" + date + "\n"
                    + path;
            // 2.计算 HMAC-SHA1
            String signature = HMACSha1(stringToSign, ak_secret);
            // 3.得到 authorization header
            String authHeader = "Auth " + ak_id + ":" + signature;
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", accept);
            conn.setRequestProperty("content-type", content_type);
            conn.setRequestProperty("date", date);
            conn.setRequestProperty("Authorization", authHeader);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(body);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            statusCode = ((HttpURLConnection) conn).getResponseCode();
            if (statusCode != 200) {
                in = new BufferedReader(new InputStreamReader(((HttpURLConnection) conn).getErrorStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (statusCode != 200) {
            throw new IOException("\nHttp StatusCode: " + statusCode + "\nErrorMessage: " + result);
        }
        return result;
    }

    /*
     * GET请求
     */
    public static String sendGet(String url, String ak_id, String ak_secret) throws Exception {
        String result = "";
        BufferedReader in = null;
        int statusCode = 200;
        try {
            URL realUrl = new URL(url);
            /*
             * http header 参数
             */
            String method = "GET";
            String accept = "application/json";
            String content_type = "application/json";
            String path = realUrl.getFile();
            String date = toGMTString(new Date());
            // 1.对body做MD5+BASE64加密
            // String bodyMd5 = MD5Base64(body);
            String stringToSign = method + "\n" + accept + "\n" + "" + "\n" + content_type + "\n" + date + "\n" + path;
            // 2.计算 HMAC-SHA1
            String signature = HMACSha1(stringToSign, ak_secret);
            // 3.得到 authorization header
            String authHeader = "Auth " + ak_id + ":" + signature;
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", accept);
            connection.setRequestProperty("content-type", content_type);
            connection.setRequestProperty("date", date);
            connection.setRequestProperty("Authorization", authHeader);
            connection.setRequestProperty("Connection", "keep-alive");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            statusCode = ((HttpURLConnection) connection).getResponseCode();
            if (statusCode != 200) {
                in = new BufferedReader(new InputStreamReader(((HttpURLConnection) connection).getErrorStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (statusCode != 200) {
            throw new IOException("\nHttp StatusCode: " + statusCode + "\nErrorMessage: " + result);
        }
        return result;
    }

    /*
     * 等同于javaScript中的 new Date().toUTCString();
     */
    public static String toGMTString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.UK);
        df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }

    public static void main(String[] args) {
        String sign = sign("10001",
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCea4yPDwNKyXHXpx8BOWTTUC0cAKqqPewW4TQlouZ6iTFajEQDEREHs4xaotcXWm9NOKYHkMU8l9rHG7TbFKukrV3ABd0I15hzwU4V2H8D4GDAhV/gh/OZBUuWrzuPn5MnXreGMqyg33SZpSf2P8Nv7CVNAM293YIYxuDDIQZmhQIDAQAB",
                toGMTString(new Date()),
                "post",
                "");
        System.out.println(sign);
        //13XYsbWzabzni5aYw9p9bX7kj0w=
    }

}
