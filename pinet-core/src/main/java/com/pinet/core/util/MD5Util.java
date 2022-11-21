package com.pinet.core.util;

import java.security.MessageDigest;

/**
 * MD5工具类
 */
public class MD5Util {

    public static String md5(String text) {
        byte[] bts;
        try {
            bts = text.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bts_hash = md.digest(bts);
            StringBuffer buf = new StringBuffer();
            for (byte b : bts_hash) {
                buf.append(String.format("%02X", b & 0xff));
            }
            return buf.toString();
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
